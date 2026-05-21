package com.app.smartcoffeemachine.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smartcoffeemachine.domain.model.MachineErrorState
import com.app.smartcoffeemachine.domain.model.MachineStateStatus
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import com.app.smartcoffeemachine.ui.view.MachineStatus
import com.app.smartcoffeemachine.ui.view.SmartCoffeeMachineContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmartCoffeeMachineViewModel(
    private val manager: StateMachineManager
) : ViewModel() {

    private val _state =
        MutableStateFlow(SmartCoffeeMachineContract.SmartCoffeeMachineState())

    val state = _state.asStateFlow()

    init {
        observeProgress()
        observeMachineStatus()
        observeErrorState()
    }

    fun onActionTrigger(
        action: SmartCoffeeMachineContract.SmartCoffeeMachineAction
    ) {
        viewModelScope.launch {

            when (action) {
                SmartCoffeeMachineContract.SmartCoffeeMachineAction.PowerOn -> {
                    manager.powerOn()
                }
                SmartCoffeeMachineContract.SmartCoffeeMachineAction.StartBrew -> {
                    manager.startBrew(state.value.brewType)
                }
                SmartCoffeeMachineContract.SmartCoffeeMachineAction.CancelBrew -> {
                    manager.cancel()
                }
                SmartCoffeeMachineContract.SmartCoffeeMachineAction.Reset -> {
                    manager.reset()
                }

                SmartCoffeeMachineContract.SmartCoffeeMachineAction.AutomaticError -> {
                    manager.onError()
                }
            }
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            manager.progress.collect { progressValue ->
                _state.update {
                    it.copy(progress = progressValue)
                }
            }
        }
    }

    private fun observeMachineStatus() {
        viewModelScope.launch {
            manager.machineStatus.collect { machineStatus ->
                _state.update {
                    when (machineStatus) {
                        MachineStateStatus.IDLE -> it.copy(
                            status = MachineStatus.IDLE,
                            isPowerOnEnabled = true,
                            isStartBrewEnabled = false,
                            isCancelBrewEnabled = false,
                            isResetEnabled = false
                        )

                        MachineStateStatus.HEATING -> it.copy(
                            status = MachineStatus.HEATING,
                            isPowerOnEnabled = false,
                            isStartBrewEnabled = false,
                            isCancelBrewEnabled = false,
                            isResetEnabled = false
                        )

                        MachineStateStatus.READY -> it.copy(
                            status = MachineStatus.READY,
                            isPowerOnEnabled = false,
                            isStartBrewEnabled = true,
                            isCancelBrewEnabled = false,
                            isResetEnabled = false
                        )

                        MachineStateStatus.BREWING -> it.copy(
                            status = MachineStatus.BREWING,
                            isPowerOnEnabled = false,
                            isStartBrewEnabled = false,
                            isCancelBrewEnabled = true,
                            isResetEnabled = false
                        )

                        MachineStateStatus.ERROR -> it.copy(
                            status = MachineStatus.ERROR,
                            isPowerOnEnabled = false,
                            isStartBrewEnabled = false,
                            isCancelBrewEnabled = false,
                            isResetEnabled = true
                        )
                    }
                }
            }
        }
    }

    fun observeErrorState() {
        viewModelScope.launch {
            manager.errorState.collect { errorState ->
                when (errorState) {
                    is MachineErrorState.Error -> {
                        _state.update {
                            it.copy(
                                errorTitle = errorState.title,
                                errorMessage = errorState.message,
                            )
                        }
                    }

                    MachineErrorState.None -> {
                        _state.update {
                            it.copy(
                                errorTitle = "",
                                errorMessage = "",
                            )
                        }
                    }
                }
            }
        }
    }
}