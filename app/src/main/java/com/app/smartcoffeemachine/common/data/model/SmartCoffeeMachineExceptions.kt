package com.app.smartcoffeemachine.common.data.model

sealed class SmartCoffeeMachineExceptions(
    message: String
) : Throwable(message) {

    sealed class Network(message: String) : SmartCoffeeMachineExceptions(message) {

        data object Timeout : Network("Connection timeout")

        data class Unhandled(
            val errorCode: Int,
            val reason: String? = null
        ) : Network(
            "Network unhandled error with code: $errorCode" +
                    (reason?.let { ", reason: $it" } ?: "")
        )
    }

    sealed class Server(message: String) : SmartCoffeeMachineExceptions(message) {

        data class InternalServerError(
            val httpErrorCode: Int,
            val reason: String? = null
        ) : Server(
            "Internal server error with code: $httpErrorCode" +
                    (reason?.let { ", reason: $it" } ?: "")
        )

        data class Conflict(
            val reason: String? = null
        ) : Server(
            reason ?: "Machine is already brewing"
        )
    }

    data class Unknown(
        val reason: String? = null
    ) : SmartCoffeeMachineExceptions(
        reason ?: "Unknown error"
    )
}