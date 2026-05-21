package com.app.smartcoffeemachine.domain.statemachine

import org.junit.jupiter.api.Assertions.*
import com.app.smartcoffeemachine.android.controller.BrewingServiceController
import com.app.smartcoffeemachine.common.data.model.SmartCoffeeMachineExceptions
import com.app.smartcoffeemachine.common.domain.model.Resource
import com.app.smartcoffeemachine.domain.model.BrewStatus
import com.app.smartcoffeemachine.domain.model.MachineErrorState
import com.app.smartcoffeemachine.domain.model.MachineStateStatus
import com.app.smartcoffeemachine.domain.usecase.AutomaticErrorUseCase
import com.app.smartcoffeemachine.domain.usecase.BrewingUseCase
import com.app.smartcoffeemachine.domain.usecase.LogTransactionUseCase
import com.app.smartcoffeemachine.domain.usecase.PowerOnUseCase
import com.app.smartcoffeemachine.domain.usecase.SaveBrewUseCase
import com.app.smartcoffeemachine.ui.view.BrewType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StateMachineManagerTest {

    private val powerOnUseCase: PowerOnUseCase =
        mockk(relaxed = true)

    private val brewingUseCase: BrewingUseCase =
        mockk(relaxed = true)

    private val automaticErrorUseCase: AutomaticErrorUseCase =
        mockk(relaxed = true)

    private val saveBrewUseCase: SaveBrewUseCase =
        mockk(relaxed = true)

    private val logTransactionUseCase: LogTransactionUseCase =
        mockk(relaxed = true)

    private val brewingServiceController: BrewingServiceController =
        mockk(relaxed = true)

    private val manager = StateMachineManager(
        powerOnUseCase = powerOnUseCase,
        brewingUseCase = brewingUseCase,
        automaticErrorUseCase = automaticErrorUseCase,
        saveBrewUseCase = saveBrewUseCase,
        logTransactionUseCase = logTransactionUseCase,
        brewingServiceController = brewingServiceController
    )

    @Test
    fun `initial state should be IDLE with zero progress and no error`() {
        assertEquals(MachineStateStatus.IDLE, manager.machineStatus.value)
        assertEquals(0, manager.progress.value)
        assertEquals(MachineErrorState.None, manager.errorState.value)
    }

    @Test
    fun `powerOn should move machine to READY when powerOn succeeds`() = runTest {

        coEvery {
            powerOnUseCase()
        } returns flowOf(Resource.Success(Unit))

        manager.powerOn()

        assertEquals(MachineStateStatus.READY, manager.machineStatus.value)
        assertEquals(0, manager.progress.value)
        assertEquals(MachineErrorState.None, manager.errorState.value)

        coVerify(exactly = 1) {
            powerOnUseCase()
        }

        coVerify(atLeast = 1) {
            logTransactionUseCase(any())
        }
    }

    @Test
    fun `powerOn should move machine to ERROR when timeout exception happens`() = runTest {

        val exception = SmartCoffeeMachineExceptions.Network.Timeout

        coEvery {
            powerOnUseCase()
        } returns flowOf(Resource.Failure(exception))

        manager.powerOn()

        assertEquals(MachineStateStatus.ERROR, manager.machineStatus.value)

        assertTrue(manager.errorState.value is MachineErrorState.Error)

        val error = manager.errorState.value as MachineErrorState.Error

        assertEquals("Timeout", error.title)
        assertEquals("Connection timeout", error.message)

        coVerify(exactly = 1) {
            powerOnUseCase()
        }
    }

    @Test
    fun `powerOn should set unknown error when unknown exception happens`() = runTest {

        val exception = SmartCoffeeMachineExceptions.Unknown()

        coEvery {
            powerOnUseCase()
        } returns flowOf(Resource.Failure(exception))

        manager.powerOn()

        assertEquals(MachineStateStatus.ERROR, manager.machineStatus.value)

        assertTrue(manager.errorState.value is MachineErrorState.Error)

        val error = manager.errorState.value as MachineErrorState.Error

        assertEquals("Unknown", error.title)
        assertEquals("Unknown error", error.message)
    }

    @Test
    fun `startBrew should move machine from READY to BREWING and start service`() = runTest {

        coEvery {
            powerOnUseCase()
        } returns flowOf(Resource.Success(Unit))

        manager.powerOn()

        manager.startBrew(BrewType.ESPRESSO)

        assertEquals(MachineStateStatus.BREWING, manager.machineStatus.value)

        coVerify(exactly = 1) {
            brewingServiceController.start()
        }
    }

    @Test
    fun `handleStartBrew should save success brew and return to READY when brewing succeeds`() = runTest {

        coEvery {
            brewingUseCase()
        } returns flowOf(Resource.Success(Unit))

        manager.handleStartBrew()

        assertEquals(MachineStateStatus.READY, manager.machineStatus.value)

        coVerify(exactly = 1) {
            saveBrewUseCase(
                match {
                    it.status == BrewStatus.SUCCESS
                }
            )
        }

        coVerify(exactly = 1) {
            brewingServiceController.stop()
        }
    }

    @Test
    fun `handleStartBrew should save failed brew and move to ERROR when conflict happens`() = runTest {

        val exception = SmartCoffeeMachineExceptions.Server.Conflict(
            reason = "Machine is already brewing"
        )

        coEvery {
            brewingUseCase()
        } returns flowOf(Resource.Failure(exception))

        manager.handleStartBrew()

        assertEquals(MachineStateStatus.ERROR, manager.machineStatus.value)

        assertTrue(manager.errorState.value is MachineErrorState.Error)

        val error = manager.errorState.value as MachineErrorState.Error

        assertEquals("Conflict", error.title)
        assertEquals("Machine is already brewing", error.message)

        coVerify(exactly = 1) {
            saveBrewUseCase(
                match {
                    it.status == BrewStatus.FAIL
                }
            )
        }
    }

    @Test
    fun `handleStartBrew should save failed brew and set internal server error`() = runTest {

        val exception = SmartCoffeeMachineExceptions.Server.InternalServerError(
            httpErrorCode = 500,
            reason = "Server down"
        )

        coEvery {
            brewingUseCase()
        } returns flowOf(Resource.Failure(exception))

        manager.handleStartBrew()

        assertEquals(MachineStateStatus.ERROR, manager.machineStatus.value)

        val error = manager.errorState.value as MachineErrorState.Error

        assertEquals("InternalServerError", error.title)
        assertEquals(
            "Internal server error with code: 500, reason: Server down",
            error.message
        )

        coVerify(exactly = 1) {
            saveBrewUseCase(
                match {
                    it.status == BrewStatus.FAIL
                }
            )
        }
    }

    @Test
    fun `cancelBrew should save cancelled brew and move back to READY from BREWING`() = runTest {

        coEvery {
            powerOnUseCase()
        } returns flowOf(Resource.Success(Unit))

        manager.powerOn()

        manager.startBrew(BrewType.MOKA)

        manager.cancelBrew()

        assertEquals(MachineStateStatus.READY, manager.machineStatus.value)

        coVerify(exactly = 1) {
            saveBrewUseCase(
                match {
                    it.brewType == BrewType.MOKA &&
                            it.status == BrewStatus.CANCEL
                }
            )
        }

        coVerify(atLeast = 1) {
            brewingServiceController.stop()
        }
    }

    @Test
    fun `handleError should do nothing when automatic error succeeds`() = runTest {

        coEvery {
            automaticErrorUseCase()
        } returns flowOf(Resource.Success(Unit))

        manager.handleError()

        assertEquals(MachineStateStatus.IDLE, manager.machineStatus.value)
        assertEquals(MachineErrorState.None, manager.errorState.value)

        coVerify(exactly = 1) {
            automaticErrorUseCase()
        }
    }

    @Test
    fun `resetMachine should clear error and move from ERROR to IDLE`() = runTest {

        val exception = SmartCoffeeMachineExceptions.Network.Timeout

        coEvery {
            powerOnUseCase()
        } returns flowOf(Resource.Failure(exception))

        manager.powerOn()

        assertEquals(MachineStateStatus.ERROR, manager.machineStatus.value)

        manager.resetMachine()

        assertEquals(MachineErrorState.None, manager.errorState.value)
        assertEquals(MachineStateStatus.IDLE, manager.machineStatus.value)
    }

    @Test
    fun `updateProgress should update progress value`() {

        manager.updateProgress(50)

        assertEquals(50, manager.progress.value)
    }

    @Test
    fun `resetProgress should reset progress to zero`() {

        manager.updateProgress(70)

        manager.resetProgress()

        assertEquals(0, manager.progress.value)
    }

    @Test
    fun `startBrewingService should call service controller start`() {

        manager.startBrewingService()

        coVerify(exactly = 1) {
            brewingServiceController.start()
        }
    }

    @Test
    fun `stopBrewingService should call service controller stop`() {

        manager.stopBrewingService()

        coVerify(exactly = 1) {
            brewingServiceController.stop()
        }
    }

    @Test
    fun `stopBrewingLoop should reset progress and stop service`() {

        manager.updateProgress(90)

        manager.stopBrewingLoop()

        assertEquals(0, manager.progress.value)

        coVerify(exactly = 1) {
            brewingServiceController.stop()
        }
    }
}