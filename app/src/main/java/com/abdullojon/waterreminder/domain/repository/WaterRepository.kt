package com.abdullojon.waterreminder.domain.repository

import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import kotlinx.coroutines.flow.Flow

interface WaterRepository {
    fun getAllIntakes(): Flow<List<WaterIntake>>
    fun getTodayTotalIntake(): Flow<Int?>
    suspend fun addIntake(amountMl: Int)
    suspend fun deleteIntake(intake: WaterIntake)
    suspend fun clearHistory()
}