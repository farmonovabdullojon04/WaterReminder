package com.abdullojon.waterreminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.abdullojon.waterreminder.data.repository.WaterRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WaterNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: WaterRepositoryImpl

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "DRINK_WATER_ACTION") {
            val pendingResult = goAsync()
            val amount = 250
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.addIntake(amount)
                } finally {
                    pendingResult.finish()
                }
            }

            NotificationManagerCompat.from(context).cancel(101)
        }
    }
}
