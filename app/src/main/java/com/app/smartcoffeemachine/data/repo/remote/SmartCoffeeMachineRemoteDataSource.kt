package com.app.smartcoffeemachine.data.repo.remote

import com.app.smartcoffeemachine.common.data.model.NetworkMethods
import com.app.smartcoffeemachine.common.domain.repo.remote.IRemoteDataSourceProvider
import com.app.smartcoffeemachine.data.model.dto.SmartCoffeeMachineResponseDto
import com.app.smartcoffeemachine.domain.repo.remote.ISmartCoffeeMachineRemoteDataSource

class SmartCoffeeMachineRemoteDataSource(private val provider: IRemoteDataSourceProvider) :
    ISmartCoffeeMachineRemoteDataSource {
    override suspend fun powerOn(): SmartCoffeeMachineResponseDto {
        return provider.request(
            method = NetworkMethods.POST,
            requestBody = null,
            path = "power-on",
            serializer = SmartCoffeeMachineResponseDto.serializer()
        )
    }

    override suspend fun brew(): SmartCoffeeMachineResponseDto {
        return provider.request(
            method = NetworkMethods.POST,
            requestBody = null,
            path = "brew",
            serializer = SmartCoffeeMachineResponseDto.serializer()
        )
    }

    override suspend fun powerError() {
        provider.request(
            method = NetworkMethods.GET,
            requestBody = null,
            path = "auto-error",
            serializer = SmartCoffeeMachineResponseDto.serializer()
        )
    }
}