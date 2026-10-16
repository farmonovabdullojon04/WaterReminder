package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.datastore.UserData
import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
   private val prefsManager: UserPreferencesManager
) {
    operator fun invoke(): Flow<UserData> = prefsManager.userData
}