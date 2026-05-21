package com.app.smartcoffeemachine.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APIResponse(
    @SerialName("message")
    val message: String,
)
