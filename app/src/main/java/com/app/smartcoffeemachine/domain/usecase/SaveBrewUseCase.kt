package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.common.domain.usecase.toFailure
import com.app.smartcoffeemachine.domain.model.Brew
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SaveBrewUseCase(private val repository: ISmartCoffeeMachineRepository) {
    operator fun invoke(domain: Brew): Flow<Resource<Unit>> = flow<Resource<Unit>> {
        emit(Resource.Success(repository.saveBrew(domain)))
    }.catch { exception ->
        emit(exception.toFailure())
    }.flowOn(Dispatchers.IO)
}