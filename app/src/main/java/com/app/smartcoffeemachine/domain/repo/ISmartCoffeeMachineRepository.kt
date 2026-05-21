package com.app.smartcoffeemachine.domain.repo

import com.app.smartcoffeemachine.domain.model.Brew

interface ISmartCoffeeMachineRepository {
    suspend fun powerOn()
    suspend fun brew()
    suspend fun saveBrew(brew: Brew)
    suspend fun powerError()
}