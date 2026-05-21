package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BrewingStateTest {

    private val manager: StateMachineManager = mockk(relaxed = true)
    private val state = BrewingState(manager)

    @Test
    fun `onEnter should start brewing service and loop`() = runTest {
        state.onEnter()

        coVerifyOrder {
            manager.startBrewingService()
            manager.startBrewingLoop()
        }
    }

    @Test
    fun `cancelBrew should stop service and loop then transition to ReadyState`() = runTest {
        state.cancelBrew()

        coVerifyOrder {
            manager.stopBrewingService()
            manager.stopBrewingLoop()
            manager.transitionTo(match { it is ReadyState })
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