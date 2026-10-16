package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.domain.repository.WaterRepository
import javax.inject.Inject

class ClearWaterHistoryUseCase @Inject constructor(
    private val repository: WaterRepository
) {
    suspend operator fun invoke(){
        repository.clearHistory()
    }
}