package com.app.smartcoffeemachine.domain.statemachine.state


import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class IdealState(private val manager: StateMachineManager): IStateMachineState() {
    override suspend fun powerOnMachine() {
        manager.handlePowerOn()
    }
    override suspend fun onError() {
        manager.onError()
    }
}