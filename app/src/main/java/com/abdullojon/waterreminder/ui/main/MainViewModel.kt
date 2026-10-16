package com.abdullojon.waterreminder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import com.abdullojon.waterreminder.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTodayWaterIntakeUseCase: GetTodayWaterIntakeUseCase,
    private val getWaterIntakeHistoryUseCase: GetWaterIntakeHistoryUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val addWaterIntakeUseCase: AddWaterIntakeUseCase,
    private val deleteWaterIntakeUseCase: DeleteWaterIntakeUseCase,
    private val setCongratsShownTodayUseCase: SetCongratsShownTodayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        observeUserData()
        observeIntakeData()
        observeHistoryData()
    }

    private fun observeUserData() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { userData ->
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                _state.update { currentState ->
                    val isGoalReached = userData.dailyGoal > 0 && currentState.totalIntakeToday >= userData.dailyGoal
                    val shouldShowCongrats = isGoalReached && userData.lastCongratsDate != todayStr

                    currentState.copy(
                        userName = userData.name,
                        dailyGoal = userData.dailyGoal,
                        isProfileCompleted = userData.isProfileCompleted,
                        lastCongratsDate = userData.lastCongratsDate,
                        showCongratsDialog = if (shouldShowCongrats) true else currentState.showCongratsDialog,
                        reminderIntervalMin = userData.reminderIntervalMin
                    )
                }

                val current = _state.value
                if (current.dailyGoal > 0 && current.totalIntakeToday >= current.dailyGoal && userData.lastCongratsDate != todayStr) {
                    setCongratsShownTodayUseCase(todayStr)
                }
            }
        }
    }

    private fun observeIntakeData() {
        viewModelScope.launch {
            getTodayWaterIntakeUseCase().collect { total ->
                val intake = total ?: 0
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                _state.update { currentState ->
                    val isGoalReached = currentState.dailyGoal > 0 && intake >= currentState.dailyGoal
                    val shouldShowCongrats = isGoalReached && currentState.lastCongratsDate != todayStr

                    currentState.copy(
                        totalIntakeToday = intake,
                        showCongratsDialog = if (shouldShowCongrats) true else currentState.showCongratsDialog
                    )
                }

                val current = _state.value
                if (current.dailyGoal > 0 && intake >= current.dailyGoal && current.lastCongratsDate != todayStr) {
                    setCongratsShownTodayUseCase(todayStr)
                } else if (intake < current.dailyGoal && current.lastCongratsDate.isNotEmpty()) {
                    setCongratsShownTodayUseCase("")
                }
            }
        }
    }

    private fun observeHistoryData() {
        viewModelScope.launch {
            getWaterIntakeHistoryUseCase().collect { history ->
                _state.update { it.copy(intakeHistory = history) }
            }
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            addWaterIntakeUseCase(amountMl)
        }
    }

    fun deleteIntake(intake: WaterIntake) {
        viewModelScope.launch {
            deleteWaterIntakeUseCase(intake)
        }
    }

    fun openCongratsDialog() {
        _state.update { it.copy(showCongratsDialog = true) }
    }

    fun dismissCongratsDialog() {
        _state.update { it.copy(showCongratsDialog = false) }
    }
}
