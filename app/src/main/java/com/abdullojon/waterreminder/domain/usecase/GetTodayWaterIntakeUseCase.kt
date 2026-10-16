package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayWaterIntakeUseCase @Inject constructor(
    private val repository: WaterRepository
) {
    operator fun invoke(): Flow<Int?> = repository.getTodayTotalIntake()
}