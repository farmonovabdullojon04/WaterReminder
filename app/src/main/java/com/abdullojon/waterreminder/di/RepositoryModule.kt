package com.abdullojon.waterreminder.di

import com.abdullojon.waterreminder.data.repository.WaterRepositoryImpl
import com.abdullojon.waterreminder.domain.repository.WaterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindWaterRepository(impl: WaterRepositoryImpl): WaterRepository
}