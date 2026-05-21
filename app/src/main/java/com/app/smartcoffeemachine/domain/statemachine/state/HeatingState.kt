package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import kotlinx.coroutines.delay

class HeatingState(private val manager: StateMachineManager) : IStateMachineState() {
    override suspend fun onEnter() {
        for (progress in 0..100 step 10) {
            delay(300)
            manager.updateProgress(progress)
        }
        manager.resetProgress()
        manager.transitionTo(ReadyState(manager))
    }
}