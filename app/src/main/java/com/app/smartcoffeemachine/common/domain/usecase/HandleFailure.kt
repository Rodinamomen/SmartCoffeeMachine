package com.app.smartcoffeemachine.common.domain.usecase

import com.app.smartcoffeemachine.common.data.model.SmartCoffeeMachineExceptions
import com.app.smartcoffeemachine.common.domain.model.Resource

fun Throwable.toFailure(): Resource.Failure {
    val failureException =
        this as? SmartCoffeeMachineExceptions
            ?: SmartCoffeeMachineExceptions.Unknown(
                reason = message
            )

    return Resource.Failure(failureException)
}