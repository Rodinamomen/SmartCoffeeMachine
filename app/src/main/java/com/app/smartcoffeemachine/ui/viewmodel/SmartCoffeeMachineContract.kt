package com.app.smartcoffeemachine.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.smartcoffeemachine.R
import com.app.smartcoffeemachine.domain.model.BrewType

sealed interface SmartCoffeeMachineContract {
    data class SmartCoffeeMachineState(
        val status: MachineStatus = MachineStatus.IDLE,
        val isPowerOnEnabled: Boolean = true,
        val isStartBrewEnabled: Boolean = false,
        val isErrorVisible: Boolean = false,
        val isCancelBrewEnabled: Boolean = false,
        val isResetEnabled: Boolean = false,
        val progress: Int = 0,
        val errorUiState : ErrorUiState = ErrorUiState(),
        val brewTypes: List<SelectBrewType> = listOf(
            SelectBrewType(
                brewType = BrewType.ESPRESSO,
                isSelected = true
            ),
            SelectBrewType(
                brewType = BrewType.LATTE,
                isSelected = false
            )
        )
    ) : SmartCoffeeMachineContract{
        val selectedBrewType: BrewType
            get() = brewTypes.first { it.isSelected }.brewType
    }

    data class SelectBrewType(
        val brewType: BrewType = BrewType.ESPRESSO,
        val isSelected: Boolean = false,
    )
    data class ErrorUiState(
        val errorTitle: String = "",
        val errorMessage: String = "",
    )

    sealed interface SmartCoffeeMachineAction : SmartCoffeeMachineContract {
        data object PowerOn : SmartCoffeeMachineAction
        data object StartBrew : SmartCoffeeMachineAction
        data object CancelBrew : SmartCoffeeMachineAction
        data object Reset : SmartCoffeeMachineAction
        data object AutomaticError : SmartCoffeeMachineAction
        data class SelectBrewType(val brewType: BrewType) : SmartCoffeeMachineAction
    }
}

enum class MachineStatus(
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int,
) {
    IDLE(
        label = R.string.idle,
        icon = R.drawable.ic_idle,
    ),
    HEATING(
        label = R.string.heating,
        icon = R.drawable.ic_heating,
    ),
    READY(
        label = R.string.ready,
        icon = R.drawable.ic_ready,
    ),
    BREWING(
        label = R.string.brewing,
        icon = R.drawable.ic_brewing,
    ),
    ERROR(
        label = R.string.fault,
        icon = R.drawable.ic_error,
    ),
}
