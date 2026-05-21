package com.app.smartcoffeemachine.domain.statemachine

abstract class IStateMachineState() {
    open suspend  fun onEnter() {}
    open suspend fun powerOn() {}
    open suspend fun startBrew() {}
    open suspend fun cancel() {}
    open suspend fun reset() {}
    open suspend fun onError() {}
}