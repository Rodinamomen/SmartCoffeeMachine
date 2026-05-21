package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ReadyStateTest {

    private val manager: StateMachineManager = mockk(relaxed = true)
    private val state = ReadyState(manager)

    @Test
    fun `startBrew should transition to BrewingState`() = runTest {
        state.startBrew()

        coVerify(exactly = 1) {
            manager.transitionTo(match { it is BrewingState })
        }
    }
}