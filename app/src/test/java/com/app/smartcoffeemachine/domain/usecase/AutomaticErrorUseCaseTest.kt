package com.app.smartcoffeemachine.domain.usecase

import app.cash.turbine.test
import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AutomaticErrorUseCaseTest {

    private val repository: ISmartCoffeeMachineRepository =
        mockk(relaxed = true)

    private val useCase = AutomaticErrorUseCase(
        repository = repository
    )

    @Test
    fun `invoke should emit Success when powerError succeeds`() = runTest {

        coEvery {
            repository.powerError()
        } returns Unit

        useCase().test {

            val result = awaitItem()

            assertTrue(result is Resource.Success)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            repository.powerError()
        }
    }

    @Test
    fun `invoke should emit Failure when powerError throws exception`() = runTest {

        coEvery {
            repository.powerError()
        } throws RuntimeException()

        useCase().test {

            val result = awaitItem()

            assertTrue(result is Resource.Failure)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            repository.powerError()
        }
    }
}