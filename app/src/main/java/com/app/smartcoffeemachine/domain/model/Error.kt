package com.app.smartcoffeemachine.domain.model

sealed interface MachineErrorState {
    data object None : MachineErrorState

    data class Error(
        val title: String,
        val message: String,
    ) : MachineErrorState
}
