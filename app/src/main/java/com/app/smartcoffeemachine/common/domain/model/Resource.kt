package com.app.smartcoffeemachine.common.domain.model

import com.app.smartcoffeemachine.common.data.model.SmartCoffeeMachineExceptions

sealed class Resource<out Model> {
    data class Success<out Model>(val model: Model) : Resource<Model>()
    data class Failure(val exception: SmartCoffeeMachineExceptions) : Resource<Nothing>()
}