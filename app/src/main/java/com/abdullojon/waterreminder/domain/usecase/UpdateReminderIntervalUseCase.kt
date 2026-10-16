package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import javax.inject.Inject

class UpdateReminderIntervalUseCase @Inject constructor(
    private val prefsManager: UserPreferencesManager
) {
    suspend operator fun invoke(intervalMin: Int) {
        prefsManager.updateReminderInterval(intervalMin)
    }
}
