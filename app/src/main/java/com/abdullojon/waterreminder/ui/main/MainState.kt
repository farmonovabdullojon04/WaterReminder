package com.abdullojon.waterreminder.ui.main

import com.abdullojon.waterreminder.data.local.entity.WaterIntake

data class MainState(
    val userName: String = "",
    val totalIntakeToday: Int = 0,
    val dailyGoal: Int = 2000,
    val intakeHistory: List<WaterIntake> = emptyList(),
    val isProfileCompleted: Boolean? = null,
    val lastCongratsDate: String = "",
    val showCongratsDialog: Boolean = false
)
