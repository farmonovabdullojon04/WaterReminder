package com.abdullojon.waterreminder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Query("SELECT * FROM water_intake ORDER BY timestamp DESC")
    fun getAllIntakes(): Flow<List<WaterIntake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(intake: WaterIntake)

    @Delete
    suspend fun deleteIntake(intake: WaterIntake)

    @Query("SELECT SUM(amountMl) FROM water_intake WHERE timestamp >= :startOfDay")
    fun getTodayTotalIntake(startOfDay: Long): Flow<Int?>

    @Query("SELECT SUM(amountMl) FROM water_intake WHERE timestamp >= :startOfDay")
    suspend fun getTodayTotalIntakeOnce(startOfDay: Long): Int?

    @Query("DELETE FROM water_intake")
    suspend fun deleteAllIntakes()
}
