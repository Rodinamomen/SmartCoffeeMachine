package com.app.smartcoffeemachine.domain.usecase

import com.app.smartcoffeemachine.data.repo.local.MachineLogger
import com.app.smartcoffeemachine.domain.model.StateTransitionLog
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LogTransactionUseCaseTest {

    private val machineLogger: MachineLogger =
        mockk(relaxed = true)

    private val useCase = LogTransactionUseCase(
        machineLogger = machineLogger
    )

    @Test
    fun `invoke should call logTransition with provided log`() = runTest {

        val log = mockk<StateTransitionLog>()

        useCase(log)

        coVerify(exactly = 1) {
            machineLogger.logTransition(log)
        }
    }
}