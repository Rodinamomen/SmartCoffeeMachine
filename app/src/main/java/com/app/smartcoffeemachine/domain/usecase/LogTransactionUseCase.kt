package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.data.repo.local.MachineLogger
import com.app.smartcoffeemachine.domain.model.StateTransitionLog

class LogTransactionUseCase(private val machineLogger: MachineLogger) {
    suspend operator fun invoke(log: StateTransitionLog) {
        machineLogger.logTransition(log)
    }
}