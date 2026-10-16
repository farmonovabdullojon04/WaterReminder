package com.abdullojon.waterreminder.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val nameKey = stringPreferencesKey("user_name")
    private val weightKey = floatPreferencesKey("user_weight")
    private val dailyGoalKey = intPreferencesKey("daily_goal")
    private val profileCompletedKey = booleanPreferencesKey("profile_completed")

    private val sleepStartKey = intPreferencesKey("sleep_start_min")
    private val sleepEndKey = intPreferencesKey("sleep_end_min")
    private val lastCongratsDateKey = stringPreferencesKey("last_congrats_date")
    private val reminderIntervalKey = intPreferencesKey("reminder_interval_min")

    val userData: Flow<UserData> = context.dataStore.data.map { prefs ->
        UserData(
            name = prefs[nameKey] ?: "",
            weight = prefs[weightKey] ?: 0f,
            dailyGoal = prefs[dailyGoalKey] ?: 2000,
            isProfileCompleted = prefs[profileCompletedKey] ?: false,
            sleepStartMin = prefs[sleepStartKey] ?: 1320,
            sleepEndMin = prefs[sleepEndKey] ?: 420,
            lastCongratsDate = prefs[lastCongratsDateKey] ?: "",
            reminderIntervalMin = prefs[reminderIntervalKey] ?: 60
        )
    }

    suspend fun saveProfile(
        name: String,
        weight: Float,
        sleepStart: Int,
        sleepEnd: Int,
        reminderIntervalMin: Int = 60
    ) {
        val goal = (weight * 35).toInt()
        context.dataStore.edit { prefs ->
            prefs[nameKey] = name
            prefs[weightKey] = weight
            prefs[dailyGoalKey] = goal
            prefs[profileCompletedKey] = true
            prefs[sleepStartKey] = sleepStart
            prefs[sleepEndKey] = sleepEnd
            prefs[reminderIntervalKey] = reminderIntervalMin.coerceAtLeast(15)
        }
    }

    suspend fun updateReminderInterval(intervalMin: Int) {
        context.dataStore.edit { prefs ->
            prefs[reminderIntervalKey] = intervalMin.coerceAtLeast(15)
        }
    }

    suspend fun setCongratsShownToday(dateStr: String) {
        context.dataStore.edit { prefs ->
            prefs[lastCongratsDateKey] = dateStr
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}

data class UserData(
    val name: String,
    val weight: Float,
    val dailyGoal: Int,
    val isProfileCompleted: Boolean,
    val sleepStartMin: Int,
    val sleepEndMin: Int,
    val lastCongratsDate: String = "",
    val reminderIntervalMin: Int = 60
)
