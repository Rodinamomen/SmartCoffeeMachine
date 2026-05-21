package com.app.smartcoffeemachine.domain.repo.remote

import com.app.smartcoffeemachine.data.model.dto.SmartCoffeeMachineResponseDto

interface ISmartCoffeeMachineRemoteDataSource {
    suspend fun powerOn(): SmartCoffeeMachineResponseDto
    suspend fun brew(): SmartCoffeeMachineResponseDto
    suspend fun powerError()
}