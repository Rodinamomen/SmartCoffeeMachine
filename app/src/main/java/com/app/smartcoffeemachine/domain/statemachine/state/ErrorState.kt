package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class ErrorState : IStateMachineState {
    override suspend fun onEnter(manager: StateMachineManager) {
    }

    override suspend fun powerOn(manager: StateMachineManager) {

    }

    override suspend fun startBrew(manager: StateMachineManager) {

    }

    override suspend fun cancel(manager: StateMachineManager) {

    }

    override suspend fun reset(manager: StateMachineManager) {
        manager.transitionTo(IdealState())
    }

    override suspend fun onError(manager: StateMachineManager) {
        manager.triggerAutomaticError()
    }
}