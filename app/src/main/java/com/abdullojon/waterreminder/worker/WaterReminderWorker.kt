package com.abdullojon.waterreminder.worker

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abdullojon.waterreminder.MainActivity
import com.abdullojon.waterreminder.data.local.dao.WaterIntakeDao
import com.abdullojon.waterreminder.data.local.datastore.UserPreferencesManager
import com.abdullojon.waterreminder.receiver.WaterNotificationReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.Calendar

@HiltWorker
class WaterReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val waterIntakeDao: WaterIntakeDao,
    private val prefsManager: UserPreferencesManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val userData = prefsManager.userData.first()

        if (isSleepTime(userData.sleepStartMin, userData.sleepEndMin)) {
            return Result.success()
        }

        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val totalIntake = waterIntakeDao.getTodayTotalIntakeOnce(startOfDay) ?: 0

        if (totalIntake < userData.dailyGoal) {
            showNotification()
        }
        
        return Result.success()
    }

    private fun isSleepTime(startMin: Int, endMin: Int): Boolean {
        val now = Calendar.getInstance()
        val currentMin = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

        return if (startMin < endMin) {
            currentMin in startMin..endMin
        } else {
            currentMin >= startMin || currentMin <= endMin
        }
    }

    private fun showNotification() {
        val channelId = "water_reminder_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Suv Eslatmasi", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val drinkIntent = Intent(applicationContext, WaterNotificationReceiver::class.java).apply {
            action = "DRINK_WATER_ACTION"
        }
        val drinkPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 1, drinkIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_menu_info_details)
            .setContentTitle("Suv ichish vaqti bo'ldi!")
            .setContentText("Sog'lig'ingiz uchun hozir 250ml suv iching.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(0, "Ichdim (250ml)", drinkPendingIntent)
            .build()

        notificationManager.notify(101, notification)
    }
}
