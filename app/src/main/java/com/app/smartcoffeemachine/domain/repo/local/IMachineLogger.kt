package com.app.smartcoffeemachine.domain.repo.local

import com.app.smartcoffeemachine.domain.model.StateTransitionLog

interface IMachineLogger {
    suspend fun logTransition(log: StateTransitionLog)
}