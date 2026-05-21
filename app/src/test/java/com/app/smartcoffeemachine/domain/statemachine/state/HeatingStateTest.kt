package com.app.smartcoffeemachine.domain.statemachine.state

import com.app.smartcoffeemachine.domain.statemachine.StateMachineManager
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HeatingStateTest {

    private val manager: StateMachineManager = mockk(relaxed = true)
    private val state = HeatingState(manager)

    @Test
    fun `onEnter should update progress then reset and transition to ReadyState`() = runTest {
        state.onEnter()

        coVerifyOrder {
            manager.updateProgress(0)
            manager.updateProgress(10)
            manager.updateProgress(20)
            manager.updateProgress(30)
            manager.updateProgress(40)
            manager.updateProgress(50)
            manager.updateProgress(60)
            manager.updateProgress(70)
            manager.updateProgress(80)
            manager.updateProgress(90)
            manager.updateProgress(100)
            manager.resetProgress()
            manager.transitionTo(match { it is ReadyState })
        }
    }
}