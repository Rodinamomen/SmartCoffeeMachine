package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.common.data.model.SmartCoffeeMachineExceptions
import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

fun Throwable.toFailure(): Resource.Failure {
    val failureException = this as? SmartCoffeeMachineExceptions ?: SmartCoffeeMachineExceptions.Unknown("")
    return Resource.Failure(failureException)
}

class PowerOnUseCase(private val repository: ISmartCoffeeMachineRepository) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Success(repository.powerOn()))
        emit(Resource.Loading(isLoading = false))
    }.onStart {
        emit(Resource.Loading(isLoading = true))
    }.catch { exception ->
        emit(exception.toFailure())
        emit(Resource.Loading(isLoading = false))
    }
}