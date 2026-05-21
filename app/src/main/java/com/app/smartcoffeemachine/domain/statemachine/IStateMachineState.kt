package com.app.smartcoffeemachine.domain.statemachine

interface IStateMachineState {
    suspend fun onEnter(manager: StateMachineManager)
    suspend fun powerOn(manager: StateMachineManager)
    suspend fun startBrew(manager: StateMachineManager)
    suspend fun cancel(manager: StateMachineManager)
    suspend fun reset(manager: StateMachineManager)
    suspend fun onError(manager: StateMachineManager)
}