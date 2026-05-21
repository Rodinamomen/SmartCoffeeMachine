package com.app.smartcoffeemachine.domain.statemachine

abstract class IStateMachineState() {
    open suspend  fun onEnter(manager: StateMachineManager){}
    open suspend fun powerOn(manager: StateMachineManager){}
    open suspend fun startBrew(manager: StateMachineManager){}
    open suspend fun cancel(manager: StateMachineManager){}
    open suspend fun reset(manager: StateMachineManager){}
    open suspend fun onError(manager: StateMachineManager){}
}