package com.app.smartcoffeemachine.domain.statemachine

abstract class IStateMachineState() {
    open suspend  fun onEnter() {}
    open suspend fun powerOnMachine() {}
    open suspend fun startBrew() {}
    open suspend fun cancelBrew() {}
    open suspend fun resetMachine() {}
    open suspend fun onError() {}
}