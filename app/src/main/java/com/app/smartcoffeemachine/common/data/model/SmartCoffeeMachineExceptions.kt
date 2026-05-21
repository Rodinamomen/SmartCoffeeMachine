package com.app.smartcoffeemachine.common.data.model

sealed class SmartCoffeeMachineExceptions(message: String?) : Throwable(message) {
    sealed class Network(override val message: String? = null) : SmartCoffeeMachineExceptions(message) {
        data class Timeout(
            override val message: String =
                "Connection timeout",
        ) : Network(message)
        data class Unhandled(val errorCode: Int, override val message: String? = null) :
            Network(message = "Network Unhandled error with code:${errorCode}, and the failure reason: $message")
    }

    sealed class Server(message: String?) : SmartCoffeeMachineExceptions(message) {
        data class InternalServerError(
            override val message: String? = null,
            val httpErrorCode: Int,
        ) : Server(message = "Internal server error with code:${httpErrorCode}, and the failure reason: $message")

        data class Conflict(
            override val message: String =
                "Machine is already brewing",
        ) : Server(message)
    }

    data class Unknown(
        override val message: String?,
    ) : SmartCoffeeMachineExceptions(message)
}