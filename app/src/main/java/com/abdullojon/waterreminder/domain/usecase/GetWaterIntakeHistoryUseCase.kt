package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import com.abdullojon.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWaterIntakeHistoryUseCase @Inject constructor(
    private val repository: WaterRepository
) {
    operator fun invoke(): Flow<List<WaterIntake>> = repository.getAllIntakes()
}