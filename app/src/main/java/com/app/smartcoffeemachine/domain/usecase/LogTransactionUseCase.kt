package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.common.domain.usecase.toFailure
import com.app.smartcoffeemachine.data.repo.local.MachineLogger
import com.app.smartcoffeemachine.domain.model.StateTransitionLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LogTransactionUseCase(private val machineLogger: MachineLogger) {
    operator fun invoke(log: StateTransitionLog): Flow<Resource<Unit>> =
        flow<Resource<Unit>> {
            emit(Resource.Success(machineLogger.logTransition(log)))
        }.catch { exception ->
            emit(exception.toFailure())
        }.flowOn(Dispatchers.IO)
}