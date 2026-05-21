package com.app.smartcoffeemachine.data.repo.local

import android.content.Context
import com.app.smartcoffeemachine.data.model.entity.BrewEntity
import com.app.smartcoffeemachine.data.repo.local.dao.BrewEntityDao
import com.app.smartcoffeemachine.domain.model.StateTransitionLog
import com.app.smartcoffeemachine.domain.repo.local.ISmartCoffeeMachineLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class SmartCoffeeMachineLocalDataSource(private val dao: BrewEntityDao): ISmartCoffeeMachineLocalDataSource {
    override suspend fun insertBrew(entity: BrewEntity) {
        dao.insertBrew(entity)
    }
}