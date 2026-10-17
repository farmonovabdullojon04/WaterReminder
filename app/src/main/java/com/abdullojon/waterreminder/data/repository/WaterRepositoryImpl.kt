package com.abdullojon.waterreminder.data.repository

import com.abdullojon.waterreminder.data.local.dao.WaterIntakeDao
import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import com.abdullojon.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterRepositoryImpl @Inject constructor(
    private val waterIntakeDao: WaterIntakeDao
): WaterRepository {

    override fun getAllIntakes(): Flow<List<WaterIntake>> = waterIntakeDao.getAllIntakes()

    override fun getTodayTotalIntake(): Flow<Int?> = flow {
        while (currentCoroutineContext().isActive) {
            val now = Calendar.getInstance()
            val startOfDay = (now.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val nextMidnight = (now.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val millisUntilMidnight = nextMidnight - System.currentTimeMillis()

            emitAll(waterIntakeDao.getTodayTotalIntake(startOfDay))

            delay(millisUntilMidnight.coerceAtLeast(1000))
        }
    }

    override suspend fun addIntake(amountMl: Int) {
       waterIntakeDao.insertIntake(WaterIntake(amountMl = amountMl))
    }

    override suspend fun deleteIntake(intake: WaterIntake) {
        waterIntakeDao.deleteIntake(intake)
    }

    override suspend fun clearHistory() {
       waterIntakeDao.deleteAllIntakes()
    }
}
