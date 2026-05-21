package com.app.smartcoffeemachine.domain.model

import java.util.UUID

data class Brew(
    val id: UUID,
    val brewType: BrewType,
    val status: BrewStatus,
    val timestamp: Long,
)

enum class BrewStatus {
    SUCCESS,
    FAIL,
    CANCEL
}
enum class BrewType {
    ESPRESSO,
    LATTE
}