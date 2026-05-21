package com.app.smartcoffeemachine.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brew_history")
data class BrewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val brewType: String,
    val status: String,
    val timestamp: Long
)