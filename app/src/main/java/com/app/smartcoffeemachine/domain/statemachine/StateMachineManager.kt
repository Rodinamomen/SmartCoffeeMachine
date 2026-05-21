package com.app.smartcoffeemachine.domain.statemachine

import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.smartcoffeemachine.android.service.Actions
import com.app.smartcoffeemachine.android.service.BrewingForegroundService
import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.domain.model.Brew
import com.app.smartcoffeemachine.domain.model.BrewStatus
import com.app.smartcoffeemachine.domain.model.MachineStateStatus
import com.app.smartcoffeemachine.domain.model.StateTransitionLog
import com.app.smartcoffeemachine.domain.statemachine.state.BrewingState
import com.app.smartcoffeemachine.domain.statemachine.state.ErrorState
import com.app.smartcoffeemachine.domain.statemachine.state.HeatingState
import com.app.smartcoffeemachine.domain.statemachine.state.IdealState
import com.app.smartcoffeemachine.domain.statemachine.state.ReadyState
import com.app.smartcoffeemachine.domain.usecase.AutomaticErrorUseCase
import com.app.smartcoffeemachine.domain.usecase.BrewingUseCase
import com.app.smartcoffeemachine.domain.usecase.LogTransactionUseCase
import com.app.smartcoffeemachine.domain.usecase.PowerOnUseCase
import com.app.smartcoffeemachine.domain.usecase.SaveBrewUseCase
import com.app.smartcoffeemachine.ui.view.BrewType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

class StateMachineManager(
    private val powerOnUseCase: PowerOnUseCase,
    private val brewingUseCase: BrewingUseCase,
    private val automaticErrorUseCase: AutomaticErrorUseCase,
    private val saveBrewUseCase: SaveBrewUseCase,
    private val logTransactionUseCase: LogTransactionUseCase,
    val context: Context,
) {
    private val transitionMutex = Mutex()
    private var brewingJob: Job? = null
    private var currentState: IStateMachineState = IdealState()
    private var currentBrewType: BrewType = BrewType.ESPRESSO
    private val currentBrewId: UUID = UUID.randomUUID()

    private val _machineStatus = MutableStateFlow(MachineStateStatus.IDLE)
    val machineStatus = _machineStatus

    private val _progress = MutableStateFlow(0)
    val progress = _progress

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage

    private val _errorCause = MutableStateFlow<String?>(null)
    val errorCause = _errorCause
    private var stateEntryTimeMs: Long = System.currentTimeMillis()
    fun setState(state: IStateMachineState) {
        currentState = state
    }

    suspend fun transitionTo(state: IStateMachineState) {
        transitionMutex.withLock {
            // Prevent transition if the current coroutine context has been cancelled
            if (!coroutineContext.isActive) return
            
            logTransaction(state)
            setState(state)
            updateMachineStateStatue(state)
            state.onEnter(this)
        }
    }

    suspend fun powerOn() {
        currentState.powerOn(this)
    }

    suspend fun startBrew(brewType: BrewType) {
        currentBrewType = brewType
        currentState.startBrew(this)
    }

    suspend fun cancel() {
        handleSaveBrew(status = BrewStatus.CANCEL)
        currentState.cancel(this)
    }

    suspend fun reset() {
        _errorMessage.value = null
        _errorCause.value = null
        currentState.reset(this)
    }

    suspend fun triggerAutomaticError() {
        automaticErrorUseCase().collect { result ->
            when (result) {
                is Resource.Failure -> {
                    val exception = result.exception
                    _errorMessage.value = exception::class.simpleName
                    _errorCause.value = exception.message
                    stopBrewingLoop(true)
                    transitionTo(ErrorState())
                }
                else -> {}
            }
        }
    }

    suspend fun onError() {
        currentState.onError(this)
    }

    suspend fun handlePowerOn() {
        powerOnUseCase().collect { result ->
            when (result) {
                is Resource.Success -> {
                    transitionTo(HeatingState())
                }

                is Resource.Failure -> {
                    transitionTo(ErrorState())
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    suspend fun handleStartBrew() {
        brewingUseCase().collect { result ->
            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    handleSaveBrew(status = BrewStatus.SUCCESS)
                    // Use false to avoid cancelling the current coroutine before transition completes
                    stopBrewingLoop(false)
                    transitionTo(ReadyState())
                }

                is Resource.Failure -> {
                    handleSaveBrew(status = BrewStatus.FAIL)
                    transitionTo(ErrorState())
                }
            }
        }
    }

    fun updateProgress(value: Int) {
        _progress.value = value
    }

    fun resetProgress() {
        _progress.value = 0
    }

    fun startBrewingLoop() {
        brewingJob = CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            handleStartBrew()
        }
    }

    fun stopBrewingLoop(cancelJob: Boolean = true) {
        if (cancelJob) {
            brewingJob?.cancel()
        }
        brewingJob = null
        resetProgress()
        context.stopService(Intent(context, BrewingForegroundService::class.java))
    }

    private suspend fun logTransaction(state: IStateMachineState) {
        val now = System.currentTimeMillis()
        val duration = now - stateEntryTimeMs
        val toName = state::class.simpleName.orEmpty()
        logTransactionUseCase(
            log = StateTransitionLog(
                from = currentState::class.simpleName.orEmpty(),
                to = toName,
                durationMs = duration,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    private fun updateMachineStateStatue(state: IStateMachineState) {
        _machineStatus.value =
            when (state) {
                is IdealState -> MachineStateStatus.IDLE

                is HeatingState -> MachineStateStatus.HEATING

                is ReadyState -> MachineStateStatus.READY

                is BrewingState -> MachineStateStatus.BREWING

                is ErrorState -> MachineStateStatus.ERROR

                else -> MachineStateStatus.IDLE
            }
    }

    private suspend fun handleSaveBrew(status: BrewStatus) {
        saveBrewUseCase(
            Brew(
                id = currentBrewId,
                brewType = currentBrewType,
                status = status,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}