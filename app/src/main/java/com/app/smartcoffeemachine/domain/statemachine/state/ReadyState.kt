package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class ReadyState(private val manager: StateMachineManager) : IStateMachineState() {
    override suspend fun startBrew() {
        manager.transitionTo(BrewingState(manager))
    }
}