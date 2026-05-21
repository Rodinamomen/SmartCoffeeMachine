package com.app.smartcoffeemachine.android.controller

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.app.smartcoffeemachine.android.service.Actions
import com.app.smartcoffeemachine.android.service.BrewingForegroundService

class BrewingServiceController(
    private val context: Context,
) {
    fun start() {
        val intent = Intent(context, BrewingForegroundService::class.java).apply {
            action = Actions.START.toString()
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop() {
        val intent = Intent(context, BrewingForegroundService::class.java).apply {
            action = Actions.STOP.toString()
        }
        context.startService(intent)
    }
}