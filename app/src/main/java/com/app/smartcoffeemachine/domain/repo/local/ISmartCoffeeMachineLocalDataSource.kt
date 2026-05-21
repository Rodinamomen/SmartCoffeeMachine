package com.app.smartcoffeemachine.domain.repo.local

import com.app.smartcoffeemachine.data.model.entity.BrewEntity

interface ISmartCoffeeMachineLocalDataSource {
    suspend fun insertBrew(entity: BrewEntity)
}