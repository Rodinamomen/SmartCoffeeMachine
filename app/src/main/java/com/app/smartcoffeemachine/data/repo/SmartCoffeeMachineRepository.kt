package com.app.smartcoffeemachine.data.repo

import com.app.smartcoffeemachine.domain.mapper.toEntity
import com.app.smartcoffeemachine.domain.model.Brew
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import com.app.smartcoffeemachine.domain.repo.local.ISmartCoffeeMachineLocalDataSource
import com.app.smartcoffeemachine.domain.repo.remote.ISmartCoffeeMachineRemoteDataSource

class SmartCoffeeMachineRepository(
    private val remote: ISmartCoffeeMachineRemoteDataSource,
    private val local: ISmartCoffeeMachineLocalDataSource,
) : ISmartCoffeeMachineRepository {
    override suspend fun powerOn() {
        remote.powerOn()
    }

    override suspend fun brew() {
        remote.brew()
    }

    override suspend fun saveBrew(brew: Brew) {
        local.insertBrew(brew.toEntity())
    }

    override suspend fun powerError() {
        remote.powerError()
    }
}