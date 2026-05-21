package com.app.smartcoffeemachine.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smartcoffeemachine.domain.model.BrewType
import com.app.smartcoffeemachine.domain.model.MachineErrorState
import com.app.smartcoffeemachine.domain.model.MachineStateStatus
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SmartCoffeeMachineViewModel(
    private val manager: StateMachineManager,
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
        action: SmartCoffeeMachineContract.SmartCoffeeMachineAction,
    ) {
        viewModelScope.launch {
            when (action) {
                is SmartCoffeeMachineContract.SmartCoffeeMachineAction.PowerOn -> {
                    manager.powerOn()
                }

                is SmartCoffeeMachineContract.SmartCoffeeMachineAction.StartBrew -> {
                    manager.startBrew(state.value.selectedBrewType)
                }

                is SmartCoffeeMachineContract.SmartCoffeeMachineAction.CancelBrew -> {
                    manager.cancelBrew()
                }

                is SmartCoffeeMachineContract.SmartCoffeeMachineAction.Reset -> {
                    manager.resetMachine()
                }

                is SmartCoffeeMachineContract.SmartCoffeeMachineAction.AutomaticError -> {
                    manager.onError()
                }

                is SmartCoffeeMachineContract.SmartCoffeeMachineAction.SelectBrewType -> {
                    updateSelectedBrewType(action.brewType)
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
                updateMachineState(machineStatus)
            }
        }
    }

    fun observeErrorState() {
        viewModelScope.launch {
            manager.errorState.collect { errorState ->
                updateErrorState(errorState)
            }
        }
    }

    private fun updateSelectedBrewType(brewType: BrewType) {
        _state.update { currentState ->
            currentState.copy(
                brewTypes = currentState.brewTypes.map { item ->
                    item.copy(
                        isSelected = item.brewType == brewType
                    )
                }
            )
        }
    }

    private fun updateMachineState(machineStatus: MachineStateStatus) {
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

    private fun updateErrorState(errorState: MachineErrorState) {
        when (errorState) {
            is MachineErrorState.Error -> {
                _state.update {
                    it.copy(
                        errorUiState = it.errorUiState.copy(
                            errorTitle = errorState.title,
                            errorMessage = errorState.message,
                        ),
                        isErrorVisible = true
                    )
                }
            }

            MachineErrorState.None -> {
                _state.update {
                    it.copy(
                        errorUiState = it.errorUiState.copy(
                            errorTitle = "",
                            errorMessage = "",
                        ),
                        isErrorVisible = false
                    )
                }
            }
        }
    }
}