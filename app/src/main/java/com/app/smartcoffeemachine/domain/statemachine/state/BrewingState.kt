package com.app.smartcoffeemachine.domain.statemachine.state

import android.content.Intent
import androidx.core.content.ContextCompat
import com.app.smartcoffeemachine.android.service.Actions
import com.app.smartcoffeemachine.android.service.BrewingForegroundService
import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class BrewingState(private val manager: StateMachineManager) : IStateMachineState() {
    override suspend fun onEnter() {
        val intent = Intent(manager.context, BrewingForegroundService::class.java).apply {
            action = Actions.START.toString()
        }
        ContextCompat.startForegroundService(manager.context, intent)
        manager.startBrewingLoop()
    }
    override suspend fun cancel() {
        val intent = Intent(manager.context, BrewingForegroundService::class.java).apply {
            action = Actions.STOP.toString()
        }
        manager.context.startService(intent)
        manager.stopBrewingLoop()
        manager.transitionTo(ReadyState(manager))
    }

    override suspend fun onError() {
        manager.transitionTo(ErrorState(manager))
    }
}