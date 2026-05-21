package com.app.smartcoffeemachine.common.data.repo.remote

import com.app.smartcoffeemachine.common.data.model.SmartCoffeeMachineExceptions
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.net.SocketTimeoutException

fun provideHttpClient() = HttpClient() {
    expectSuccess = true
    install(ContentNegotiation) {
        json()
    }
    install(Logging) {
        level = LogLevel.ALL
        level = LogLevel.ALL
    }
    install(HttpRequestRetry) {
        maxRetries = 3
        retryIf { _, response ->
            response.status.value >= 500
        }
        retryOnExceptionIf { _, cause ->
            cause is HttpRequestTimeoutException ||
                    cause is ConnectTimeoutException ||
                    cause is SocketTimeoutException
        }
        exponentialDelay()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 60_000
        socketTimeoutMillis = 20_000
    }
    defaultRequest {
        // TODO base url form build.Config
        url("http://192.168.1.22:3001/machine/")
        contentType(ContentType.Application.Json)
    }
    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, _ ->
            if (exception is ResponseException) {
                throw mapHttpException(exception.response)
            }
            throw SmartCoffeeMachineExceptions.Unknown(exception.message)
        }
    }
}

private fun mapHttpException(
    response: HttpResponse,
): SmartCoffeeMachineExceptions {
    val statusCode = response.status.value
    val description = response.status.description
    return when (statusCode) {
        HttpStatusCode.InternalServerError.value ->
            SmartCoffeeMachineExceptions.Server.InternalServerError(
                httpErrorCode = statusCode,
                message = description
            )

        HttpStatusCode.Conflict.value ->
            SmartCoffeeMachineExceptions.Server.Conflict()

        HttpStatusCode.RequestTimeout.value ->
            SmartCoffeeMachineExceptions.Network.Timeout()

        else ->
            SmartCoffeeMachineExceptions.Network.Unhandled(
                errorCode = statusCode,
                message = description
            )
    }
}