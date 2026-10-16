package com.abdullojon.waterreminder.data.repository

import com.abdullojon.waterreminder.data.local.dao.WaterIntakeDao
import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import com.abdullojon.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterRepositoryImpl @Inject constructor(
    private val waterIntakeDao: WaterIntakeDao
): WaterRepository {
    override fun getAllIntakes(): Flow<List<WaterIntake>> = waterIntakeDao.getAllIntakes()

    override fun getTodayTotalIntake(): Flow<Int?> {
        val calendar= Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)
        }
        return waterIntakeDao.getTodayTotalIntake(calendar.timeInMillis)
    }

    override suspend fun addIntake(amountMl: Int) {
       waterIntakeDao.insertIntake(WaterIntake(amountMl=amountMl))
    }

    override suspend fun deleteIntake(intake: WaterIntake) {
        waterIntakeDao.deleteIntake(intake)
    }

    override suspend fun clearHistory() {
       waterIntakeDao.deleteAllIntakes()
    }

}
