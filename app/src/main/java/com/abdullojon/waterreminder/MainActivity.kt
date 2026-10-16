package com.abdullojon.waterreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.abdullojon.waterreminder.navigation.AppNavigationHandler
import com.abdullojon.waterreminder.ui.main.MainScreen
import com.abdullojon.waterreminder.ui.main.MainViewModel
import com.abdullojon.waterreminder.ui.profile.ProfileScreen
import com.abdullojon.waterreminder.ui.theme.WaterReminderTheme
import com.abdullojon.waterreminder.worker.WaterReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: AppNavigationHandler

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()
        setupWaterReminder()

        setContent {
            WaterReminderTheme {
                val mainViewModel: MainViewModel = hiltViewModel()
                val state by mainViewModel.state.collectAsState()

                when (state.isProfileCompleted) {
                    true -> {
                        Navigator(screen = MainScreen()) { navigator ->
                            LaunchedEffect(navigationHandler) {
                                navigationHandler.navigationFlow.collect { param ->
                                    param.invoke(navigator)
                                }
                            }
                            CurrentScreen()
                        }
                    }
                    false -> {
                        Navigator(screen = ProfileScreen()) { navigator ->
                            LaunchedEffect(navigationHandler) {
                                navigationHandler.navigationFlow.collect { param ->
                                    param.invoke(navigator)
                                }
                            }
                            CurrentScreen()
                        }
                    }
                    null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF8FAFC))
                        )
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupWaterReminder() {
        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WaterReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
