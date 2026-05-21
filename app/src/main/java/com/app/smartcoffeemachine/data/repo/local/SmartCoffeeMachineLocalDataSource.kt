package com.app.smartcoffeemachine.data.repo.local

import com.app.smartcoffeemachine.data.model.entity.BrewEntity
import com.app.smartcoffeemachine.data.repo.local.dao.BrewEntityDao
import com.app.smartcoffeemachine.domain.repo.local.ISmartCoffeeMachineLocalDataSource

class SmartCoffeeMachineLocalDataSource(private val dao: BrewEntityDao) : ISmartCoffeeMachineLocalDataSource {
    override suspend fun insertBrew(entity: BrewEntity) {
        dao.insertBrew(entity)
    }
}