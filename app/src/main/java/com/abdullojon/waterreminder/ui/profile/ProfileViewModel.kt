package com.abdullojon.waterreminder.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import com.abdullojon.waterreminder.domain.usecase.SaveUserProfileUseCase
import com.abdullojon.waterreminder.navigation.AppNavigator
import com.abdullojon.waterreminder.ui.main.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    fun saveProfile(name: String, weight: Float, sleepStart: Int, sleepEnd: Int) {
        viewModelScope.launch {
            saveUserProfileUseCase(name, weight, sleepStart, sleepEnd)
            appNavigator.replaceAll(MainScreen())
        }
    }
}
