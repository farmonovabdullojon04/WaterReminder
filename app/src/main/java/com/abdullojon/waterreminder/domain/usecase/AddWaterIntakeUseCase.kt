package com.abdullojon.waterreminder.domain.usecase

import com.abdullojon.waterreminder.domain.repository.WaterRepository
import javax.inject.Inject

class AddWaterIntakeUseCase @Inject constructor(
    private val repository: WaterRepository
){
    suspend operator fun invoke(amountMl: Int){
        if (amountMl > 0){
            repository.addIntake(amountMl)
        }
    }
}