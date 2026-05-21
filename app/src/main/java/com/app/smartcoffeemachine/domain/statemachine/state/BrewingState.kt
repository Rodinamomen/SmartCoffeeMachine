package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class BrewingState(private val manager: StateMachineManager) : IStateMachineState() {
    override suspend fun onEnter() {
        manager.startBrewingService()
        manager.startBrewingLoop()
    }

    override suspend fun cancelBrew() {
        manager.stopBrewingService()
        manager.stopBrewingLoop()
        manager.transitionTo(ReadyState(manager))
    }

    override suspend fun onError() {
        manager.handleError()
    }
}