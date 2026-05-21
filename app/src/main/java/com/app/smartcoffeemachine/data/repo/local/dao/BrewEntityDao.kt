package com.app.smartcoffeemachine.data.repo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.app.smartcoffeemachine.data.model.entity.BrewEntity

@Dao
interface BrewEntityDao {
    @Insert
    suspend fun insertBrew(entity: BrewEntity)
}