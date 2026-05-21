package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class ErrorState(private val manager: StateMachineManager) : IStateMachineState() {
    override suspend fun onEnter() {
        manager.stopBrewingService()
        manager.stopBrewingLoop()
    }

    override suspend fun reset() {
        manager.transitionTo(IdealState(manager))
    }

    override suspend fun onError() {
        manager.triggerAutomaticError()
    }
}