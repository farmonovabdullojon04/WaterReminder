package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import com.abdullojon.waterreminder.domain.repository.WaterRepository
import javax.inject.Inject

class ResetUserDataUseCase @Inject constructor(
    private val repository: WaterRepository,
    private val prefsManager: UserPreferencesManager
) {
    suspend operator fun invoke(){
        repository.clearHistory()
        prefsManager.logout()
    }
}