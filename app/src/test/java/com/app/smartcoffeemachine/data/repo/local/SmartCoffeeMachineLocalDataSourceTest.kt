package com.app.smartcoffeemachine.data.repo.local

import com.app.smartcoffeemachine.data.model.entity.BrewEntity
import com.app.smartcoffeemachine.data.repo.local.dao.BrewEntityDao
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SmartCoffeeMachineLocalDataSourceTest {

    private val dao: BrewEntityDao = mockk(relaxed = true)

    private val localDataSource =
        SmartCoffeeMachineLocalDataSource(dao)

    @Test
    fun `insertBrew should call dao insertBrew`() = runTest {

        val entity = BrewEntity(
            id = 0,
            brewType = "ESPRESSO",
            status = "SUCCESS",
            timestamp = 123456789L
        )

        localDataSource.insertBrew(entity)

        coVerify(exactly = 1) {
            dao.insertBrew(entity)
        }
    }
}