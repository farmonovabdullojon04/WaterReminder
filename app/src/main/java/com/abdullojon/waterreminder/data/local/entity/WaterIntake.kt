package com.abdullojon.waterreminder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)
