package com.abdullojon.waterreminder.di

import com.abdullojon.waterreminder.navigation.AppNavigationDispatcher
import com.abdullojon.waterreminder.navigation.AppNavigationHandler
import com.abdullojon.waterreminder.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    @Singleton
    fun bindAppNavigator(impl: AppNavigationDispatcher): AppNavigator

    @Binds
    @Singleton
    fun bindAppNavigationHandler(impl: AppNavigationDispatcher): AppNavigationHandler
}
