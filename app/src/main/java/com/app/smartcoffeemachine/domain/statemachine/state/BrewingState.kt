package com.app.smartcoffeemachine.domain.statemachine.state

import android.content.Intent
import androidx.core.content.ContextCompat
import com.app.smartcoffeemachine.android.service.Actions
import com.app.smartcoffeemachine.android.service.BrewingForegroundService
import com.app.smartcoffeemachine.domain.statemachine.IStateMachineState
import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager

class BrewingState : IStateMachineState {
    override suspend fun onEnter(manager: StateMachineManager) {
        val intent = Intent(manager.context, BrewingForegroundService::class.java).apply {
            action = Actions.START.toString()
        }
        ContextCompat.startForegroundService(manager.context, intent)
        manager.startBrewingLoop()
    }

    override suspend fun powerOn(manager: StateMachineManager) {

    }

    override suspend fun startBrew(manager: StateMachineManager) {
    }

    override suspend fun cancel(manager: StateMachineManager) {
        val intent = Intent(manager.context, BrewingForegroundService::class.java).apply {
            action = Actions.STOP.toString()
        }
        manager.context.startService(intent)
        manager.stopBrewingLoop()
        manager.transitionTo(ReadyState())
    }

    override suspend fun reset(manager: StateMachineManager) {
        val intent = Intent(manager.context, BrewingForegroundService::class.java).apply {
            action = Actions.STOP.toString()
        }
        manager.context.startService(intent)
        manager.stopBrewingLoop()
        manager.transitionTo(IdealState())
    }

    override suspend fun onError(manager: StateMachineManager) {
        manager.transitionTo(ErrorState())
    }
}