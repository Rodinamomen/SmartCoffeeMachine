package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.domain.model.Brew
import com.app.smartcoffeemachine.domain.model.BrewStatus
import com.app.smartcoffeemachine.domain.repo.ISmartCoffeeMachineRepository
import com.app.smartcoffeemachine.ui.view.BrewType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.UUID

class SaveBrewUseCaseTest {

    private val repository: ISmartCoffeeMachineRepository =
        mockk(relaxed = true)

    private val useCase = SaveBrewUseCase(
        repository = repository
    )

    @Test
    fun `invoke should call saveBrew with provided brew`() = runTest {

        val brew = Brew(
            id = UUID.randomUUID(),
            brewType = BrewType.ESPRESSO,
            status = BrewStatus.SUCCESS,
            timestamp = 123456789L
        )

        useCase(brew)

        coVerify(exactly = 1) {
            repository.saveBrew(brew)
        }
    }
}