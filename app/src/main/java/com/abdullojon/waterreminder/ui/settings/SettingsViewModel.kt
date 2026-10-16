package com.abdullojon.waterreminder.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullojon.waterreminder.data.local.datastore.UserData
import com.abdullojon.waterreminder.domain.usecase.GetUserProfileUseCase
import com.abdullojon.waterreminder.domain.usecase.ResetUserDataUseCase
import com.abdullojon.waterreminder.domain.usecase.SaveUserProfileUseCase
import com.abdullojon.waterreminder.domain.usecase.UpdateReminderIntervalUseCase
import com.abdullojon.waterreminder.navigation.AppNavigator
import com.abdullojon.waterreminder.ui.profile.ProfileScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val updateReminderIntervalUseCase: UpdateReminderIntervalUseCase,
    private val resetUserDataUseCase: ResetUserDataUseCase,
    private val appNavigator: AppNavigator
) : ViewModel() {

    val userData: StateFlow<UserData> = getUserProfileUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserData("", 0f, 2000, false, 1320, 420)
        )

    fun updateProfile(name: String, weight: Float, sleepStart: Int, sleepEnd: Int, reminderIntervalMin: Int) {
        viewModelScope.launch {
            saveUserProfileUseCase(name, weight, sleepStart, sleepEnd)
            updateReminderIntervalUseCase(reminderIntervalMin)
        }
    }

    fun resetData() {
        viewModelScope.launch {
            resetUserDataUseCase()
            appNavigator.replaceAll(ProfileScreen())
        }
    }
}
