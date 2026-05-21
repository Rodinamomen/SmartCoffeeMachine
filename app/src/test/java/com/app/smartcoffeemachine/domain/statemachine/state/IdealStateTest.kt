package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class IdealStateTest {

    private val manager: StateMachineManager = mockk(relaxed = true)
    private val state = IdealState(manager)

    @Test
    fun `powerOnMachine should call handlePowerOn`() = runTest {
        state.powerOnMachine()

        coVerify(exactly = 1) {
            manager.handlePowerOn()
        }
    }

    @Test
    fun `onError should call onError from manager`() = runTest {
        state.onError()

        coVerify(exactly = 1) {
            manager.onError()
        }
    }
}