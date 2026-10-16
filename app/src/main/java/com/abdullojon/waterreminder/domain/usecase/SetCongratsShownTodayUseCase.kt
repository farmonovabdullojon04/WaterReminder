package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import javax.inject.Inject

class SetCongratsShownTodayUseCase @Inject constructor(
    private val prefsManager: UserPreferencesManager
) {
    suspend operator fun invoke(dateStr: String){
        prefsManager.setCongratsShownToday(dateStr)
    }
}