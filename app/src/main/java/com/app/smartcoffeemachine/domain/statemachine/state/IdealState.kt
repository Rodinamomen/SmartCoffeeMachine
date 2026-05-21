package com.app.smartcoffeemachine.domain.statemachine.state


import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class IdealState : IStateMachineState() {
    override suspend fun powerOn(manager: StateMachineManager) {
        manager.handlePowerOn()
    }
    override suspend fun onError(manager: StateMachineManager) {
        manager.onError()
    }
}