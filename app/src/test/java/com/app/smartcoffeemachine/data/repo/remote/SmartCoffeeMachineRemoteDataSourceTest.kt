package com.app.smartcoffeemachine.data.repo.remote

import com.app.smartcoffeemachine.common.data.model.NetworkMethods
import com.app.smartcoffeemachine.common.domain.repo.remote.IRemoteDataSourceProvider
import com.app.smartcoffeemachine.data.model.dto.SmartCoffeeMachineResponseDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SmartCoffeeMachineRemoteDataSourceTest {

    private val provider: IRemoteDataSourceProvider = mockk()
    private val remoteDataSource = SmartCoffeeMachineRemoteDataSource(provider)

    @Test
    fun `powerOn calls provider with power-on path and returns response`() = runTest {
        val expected = SmartCoffeeMachineResponseDto(message = "Machine powered on")

        coEvery {
            provider.request(
                method = NetworkMethods.POST,
                requestBody = null,
                path = "power-on",
                serializer = SmartCoffeeMachineResponseDto.serializer()
            )
        } returns expected

        val result = remoteDataSource.powerOn()

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            provider.request(
                method = NetworkMethods.POST,
                requestBody = null,
                path = "power-on",
                serializer = SmartCoffeeMachineResponseDto.serializer()
            )
        }
    }

    @Test
    fun `brew calls provider with brew path and returns response`() = runTest {
        val expected = SmartCoffeeMachineResponseDto(message = "Brewing started")

        coEvery {
            provider.request(
                method = NetworkMethods.POST,
                requestBody = null,
                path = "brew",
                serializer = SmartCoffeeMachineResponseDto.serializer()
            )
        } returns expected

        val result = remoteDataSource.brew()

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            provider.request(
                method = NetworkMethods.POST,
                requestBody = null,
                path = "brew",
                serializer = SmartCoffeeMachineResponseDto.serializer()
            )
        }
    }

    @Test
    fun `powerError calls provider with auto-error path`() = runTest {
        val expected = SmartCoffeeMachineResponseDto(message = "Automatic error")

        coEvery {
            provider.request(
                method = NetworkMethods.GET,
                requestBody = null,
                path = "auto-error",
                serializer = SmartCoffeeMachineResponseDto.serializer()
            )
        } returns expected

        remoteDataSource.powerError()

        coVerify(exactly = 1) {
            provider.request(
                method = NetworkMethods.GET,
                requestBody = null,
                path = "auto-error",
                serializer = SmartCoffeeMachineResponseDto.serializer()
            )
        }
    }
}