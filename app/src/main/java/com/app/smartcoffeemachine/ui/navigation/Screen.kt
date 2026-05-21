package com.app.smartcoffeemachine.ui.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object SmartCoffeeMachine : Screen()
}