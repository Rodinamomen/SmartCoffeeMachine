package com.app.smartcoffeemachine.common.data.repo.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.smartcoffeemachine.data.model.entity.BrewEntity
import com.app.smartcoffeemachine.data.repo.local.dao.BrewEntityDao

@Database(
    entities = [
        BrewEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun BrewEntityDao(): BrewEntityDao
}