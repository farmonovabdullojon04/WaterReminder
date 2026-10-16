package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.data.local.entity.WaterIntake
import com.abdullojon.waterreminder.domain.repository.WaterRepository
import javax.inject.Inject

class DeleteWaterIntakeUseCase @Inject constructor(
    private val repository: WaterRepository
) {
    suspend operator fun invoke(intake: WaterIntake){
        repository.deleteIntake(intake)
    }
}