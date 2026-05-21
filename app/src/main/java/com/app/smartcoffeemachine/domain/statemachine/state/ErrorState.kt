package com.app.smartcoffeemachine.domain.statemachine.state

import android.content.Intent
import com.app.smartcoffeemachine.android.service.Actions
import com.app.smartcoffeemachine.android.service.BrewingForegroundService
import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class ErrorState(private val manager: StateMachineManager) : IStateMachineState() {
    override suspend fun onEnter() {
            val intent = Intent(manager.context, BrewingForegroundService::class.java).apply {
                action = Actions.STOP.toString()
            }
            manager.context.startService(intent)
            manager.stopBrewingLoop()
    }

    override suspend fun reset() {
        manager.transitionTo(IdealState(manager))
    }

    override suspend fun onError() {
        manager.triggerAutomaticError()
    }
}