package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class BrewingUseCase(private val repository: ISmartCoffeeMachineRepository) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Success(repository.brew()))
        emit(Resource.Loading(isLoading = false))
    }.onStart {
        emit(Resource.Loading(isLoading = true))
    }.catch { exception ->
        emit(exception.toFailure())
        emit(Resource.Loading(isLoading = false))
    }.flowOn(Dispatchers.IO)
}