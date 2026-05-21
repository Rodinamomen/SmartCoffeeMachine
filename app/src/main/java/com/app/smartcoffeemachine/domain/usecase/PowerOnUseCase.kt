package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.common.domain.usecase.toFailure
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlin.Unit

class PowerOnUseCase(private val repository: ISmartCoffeeMachineRepository) {
    operator fun invoke(): Flow<Resource<Unit>> = flow<Resource<Unit>> {
        emit(Resource.Success(repository.powerOn()))
    }.catch { exception ->
        emit(exception.toFailure())
    }
}