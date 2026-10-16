package com.abdullojon.waterreminder.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import com.abdullojon.waterreminder.domain.usecase.ClearWaterHistoryUseCase
import com.abdullojon.waterreminder.domain.usecase.DeleteWaterIntakeUseCase
import com.abdullojon.waterreminder.domain.usecase.GetWaterIntakeHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getWaterIntakeHistoryUseCase: GetWaterIntakeHistoryUseCase,
    private val deleteWaterIntakeUseCase: DeleteWaterIntakeUseCase,
    private val clearWaterHistoryUseCase: ClearWaterHistoryUseCase
) : ViewModel() {

    val historyList: StateFlow<List<WaterIntake>> = getWaterIntakeHistoryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteIntake(intake: WaterIntake) {
        viewModelScope.launch {
            deleteWaterIntakeUseCase(intake)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            clearWaterHistoryUseCase()
        }
    }
}
