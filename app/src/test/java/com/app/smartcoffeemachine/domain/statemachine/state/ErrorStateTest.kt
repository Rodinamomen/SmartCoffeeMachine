package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ErrorStateTest {

    private val manager: StateMachineManager = mockk(relaxed = true)
    private val state = ErrorState(manager)

    @Test
    fun `onEnter should stop brewing service and loop`() = runTest {
        state.onEnter()

        coVerifyOrder {
            manager.stopBrewingService()
            manager.stopBrewingLoop()
        }
    }

    @Test
    fun `resetMachine should transition to IdealState`() = runTest {
        state.resetMachine()

        coVerify(exactly = 1) {
            manager.transitionTo(match { it is IdleState })
        }
    }

    @Test
    fun `onError should call handleError`() = runTest {
        state.onError()

        coVerify(exactly = 1) {
            manager.handleError()
        }
    }
}