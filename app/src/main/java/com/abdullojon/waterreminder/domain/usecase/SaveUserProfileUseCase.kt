package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import javax.inject.Inject
import javax.inject.Named

class SaveUserProfileUseCase @Inject constructor(
    private val prefsManager: UserPreferencesManager
) {
    suspend operator fun invoke(name: String,weight:Float,sleepStart: Int,sleepEnd: Int){
        if (name.isNotBlank() && weight> 0f){
            prefsManager.saveProfile(name,weight,sleepStart,sleepEnd)
        }
    }
}