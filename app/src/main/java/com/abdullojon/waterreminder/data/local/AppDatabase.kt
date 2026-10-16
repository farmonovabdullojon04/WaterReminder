package com.abdullojon.waterreminder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdullojon.waterreminder.data.local.dao.WaterIntakeDao
import com.abdullojon.waterreminder.data.local.entity.WaterIntake

@Database(entities = [WaterIntake::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?=null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE?:synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "water_reminder_db"
                ).build()
                INSTANCE=instance
                instance
            }
        }
    }
}
