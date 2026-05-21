package com.app.smartcoffeemachine.data.repo

import com.app.smartcoffeemachine.domain.model.Brew
import com.app.smartcoffeemachine.domain.model.BrewStatus
import com.app.smartcoffeemachine.domain.repo.local.ISmartCoffeeMachineLocalDataSource
import com.app.smartcoffeemachine.domain.repo.remote.ISmartCoffeeMachineRemoteDataSource
import com.app.smartcoffeemachine.ui.view.BrewType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import java.util.UUID

class SmartCoffeeMachineRepositoryTest {

    private val remote: ISmartCoffeeMachineRemoteDataSource =
        mockk(relaxed = true)

    private val local: ISmartCoffeeMachineLocalDataSource =
        mockk(relaxed = true)

    private val repository = SmartCoffeeMachineRepository(
        remote = remote,
        local = local
    )

    @Test
    fun `powerOn should call remote powerOn`() = runTest {

        repository.powerOn()

        coVerify(exactly = 1) {
            remote.powerOn()
        }
    }

    @Test
    fun `brew should call remote brew`() = runTest {

        repository.brew()

        coVerify(exactly = 1) {
            remote.brew()
        }
    }

    @Test
    fun `powerError should call remote powerError`() = runTest {

        repository.powerError()

        coVerify(exactly = 1) {
            remote.powerError()
        }
    }

    @Test
    fun `saveBrew should insert mapped brew entity`() = runTest {

        val brew = Brew(
            id = UUID.randomUUID(),
            brewType = BrewType.ESPRESSO,
            status = BrewStatus.SUCCESS,
            timestamp = 123456789L
        )

        repository.saveBrew(brew)

        coVerify(exactly = 1) {
            local.insertBrew(
                match {
                    it.id == 0 &&
                            it.brewType == "ESPRESSO" &&
                            it.status == "SUCCESS" &&
                            it.timestamp == 123456789L
                }
            )
        }
    }
}