package com.app.smartcoffeemachine.domain.statemachine.state

import android.util.Log
import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class ReadyState : IStateMachineState() {
    override suspend fun startBrew(manager: StateMachineManager) {
        manager.transitionTo(BrewingState())
    }
}