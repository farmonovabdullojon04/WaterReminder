package com.abdullojon.waterreminder.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigationHandler {
    val navigationFlow: Flow<AppNavigationParam>
}
