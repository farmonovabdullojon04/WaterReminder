package com.abdullojon.waterreminder.di

import android.content.Context
import androidx.room.Room
import com.abdullojon.waterreminder.data.local.AppDatabase
import com.abdullojon.waterreminder.data.local.dao.WaterIntakeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "water_reminder_db"
        ).build()
    }

    @Provides
    fun provideWaterIntakeDao(database: AppDatabase): WaterIntakeDao {
        return database.waterIntakeDao()
    }
}
