package com.app.smartcoffeemachine.domain.model

data class StateTransitionLog(
    val from: String,
    val to: String,
    val durationMs: Long,
    val timestamp: Long,
)