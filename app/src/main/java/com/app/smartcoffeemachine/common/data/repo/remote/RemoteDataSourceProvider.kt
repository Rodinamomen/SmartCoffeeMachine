package com.app.smartcoffeemachine.common.data.repo.remote

import com.app.smartcoffeemachine.common.data.model.NetworkMethods
import com.app.smartcoffeemachine.common.domain.repo.remote.IRemoteDataSourceProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class RemoteDataSourceProvider(private val client: HttpClient, private val json: Json) : IRemoteDataSourceProvider {
    override suspend fun <ResponseBody, RequestBody> request(
        method: NetworkMethods,
        path: String,
        params: Map<String, Any>?,
        headers: Map<String, Any>?,
        requestBody: RequestBody?,
        serializer: KSerializer<ResponseBody>,
    ): ResponseBody {
        val response: HttpResponse = when (method) {
            NetworkMethods.POST -> {
                if (isBaseUrl(path)) {
                    client.post() {
                        configureUrl(path)
                        configureParams(params)
                        configureHeaders(headers)
                        requestBody?.let { setBody(it) }
                    }

                } else {
                    client.post(path) {
                        configureParams(params)
                        configureHeaders(headers)
                        requestBody?.let { setBody(it) }
                    }
                }
            }

            NetworkMethods.GET -> {
                if (isBaseUrl(path)) {
                    client.get {
                        configureUrl(path)
                        configureParams(params)
                        configureHeaders(headers)
                    }
                } else {
                    client.get(path) {
                        configureParams(params)
                        configureHeaders(headers)
                    }
                }
            }

            NetworkMethods.PUT -> {
                if (isBaseUrl(path)) {
                    client.put {
                        configureUrl(path)
                        configureParams(params)
                        configureHeaders(headers)
                        requestBody?.let { setBody(it) }
                    }
                } else {
                    client.put(path) {
                        configureParams(params)
                        configureHeaders(headers)
                        requestBody?.let { setBody(it) }
                    }
                }
            }

            NetworkMethods.DELETE -> {
                if (isBaseUrl(path)) {
                    client.delete {
                        configureUrl(path)
                        configureParams(params)
                        configureHeaders(headers)
                    }
                } else {
                    client.delete(path) {
                        configureParams(params)
                        configureHeaders(headers)
                    }
                }
            }
        }
        return handleResponse(response, serializer)
    }

    private fun isBaseUrl(path: String): Boolean {
        return path.startsWith("http")
    }

    private fun HttpRequestBuilder.configureUrl(path: String) = url(path)
    private fun HttpRequestBuilder.configureParams(params: Map<String, Any>?) {
        params?.forEach { (key, value) ->
            url.parameters.append(key, value.toString())
        }
    }

    private fun HttpRequestBuilder.configureHeaders(headers: Map<String, Any>?) {
        headers?.forEach { (key, value) ->
            header(key, value)
        }
    }

    private suspend fun <ResponseBody> handleResponse(
        response: HttpResponse,
        serializer: KSerializer<ResponseBody>,
    ): ResponseBody {
        val responseBodyText = response.bodyAsText()
        return json.decodeFromString(serializer, responseBodyText)
    }
}