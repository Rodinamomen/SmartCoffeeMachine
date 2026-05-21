package com.app.smartcoffeemachine.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SmartCoffeeMachineResponseDto(
    @SerialName("message")
    val message : String ? =  null)