package com.abdullojon.waterreminder.ui.main

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.abdullojon.waterreminder.ui.components.WaveWaterGlass
import com.abdullojon.waterreminder.ui.history.HistoryScreen
import com.abdullojon.waterreminder.ui.history.HistoryViewModel
import com.abdullojon.waterreminder.ui.result.GoalResultScreen
import com.abdullojon.waterreminder.ui.settings.SettingsScreen
import com.abdullojon.waterreminder.ui.settings.SettingsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: MainViewModel = hiltViewModel()
        MainScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Asosiy") },
                    label = { Text("Asosiy") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E88E5),
                        indicatorColor = Color(0xFFE3F2FD)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.History, contentDescription = "Tarix") },
                    label = { Text("Tarix") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E88E5),
                        indicatorColor = Color(0xFFE3F2FD)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Sozlamalar") },
                    label = { Text("Sozlamalar") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1E88E5),
                        indicatorColor = Color(0xFFE3F2FD)
                    )
                )
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> HomeScreenContent(viewModel)
                1 -> {
                    val historyViewModel: HistoryViewModel = hiltViewModel()
                    HistoryScreen(historyViewModel)
                }
                2 -> {
                    val settingsViewModel: SettingsViewModel = hiltViewModel()
                    SettingsScreen(settingsViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    var showCustomDialog by remember { mutableStateOf(false) }

    val rawProgress = if (state.dailyGoal > 0) state.totalIntakeToday.toFloat() / state.dailyGoal else 0f
    val percentage = (rawProgress * 100).toInt()

    if (state.showCongratsDialog) {
        GoalResultScreen(
            userName = state.userName,
            isAchieved = state.totalIntakeToday >= state.dailyGoal,
            onGoToHome = { viewModel.dismissCongratsDialog() }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = if (state.userName.isNotEmpty()) "Salom, ${state.userName}! 👋" else "Xush kelibsiz! 👋",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Bugungi suv balansini to'ldiring",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.openCongratsDialog() }) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = "Natijani ko'rish",
                                tint = Color(0xFFFFB300)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color(0xFF1A237E)
                    )
                )
            },
            containerColor = Color(0xFFF8FAFC)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Animated Water Wave Glass Box
                Box(contentAlignment = Alignment.Center) {
                    WaveWaterGlass(
                        progress = rawProgress,
                        sizeDp = 270.dp,
                        onClick = {
                            viewModel.addWater(250)
                        }
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$percentage%",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (rawProgress > 0.45f) Color.White else Color(0xFF1E88E5)
                        )
                        Text(
                            text = "${state.totalIntakeToday} / ${state.dailyGoal} ml",
                            color = if (rawProgress > 0.5f) Color(0xFFE1F5FE) else Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Suv ichganingizni belgilang",
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        )

                        if (state.intakeHistory.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    val latest = state.intakeHistory.firstOrNull()
                                    if (latest != null) {
                                        viewModel.deleteIntake(latest)
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = "Bekor qilish",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF1E88E5)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Qaytarish", fontSize = 12.sp, color = Color(0xFF1E88E5))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WaterButton(amount = 200, onClick = { viewModel.addWater(200) })
                        WaterButton(amount = 350, onClick = { viewModel.addWater(350) })
                        WaterButton(amount = 500, onClick = { viewModel.addWater(500) })
                        CustomWaterButton(onClick = { showCustomDialog = true })
                    }
                }
            }
        }

        if (showCustomDialog) {
            CustomAmountDialog(
                onDismiss = { showCustomDialog = false },
                onConfirm = { amount -> viewModel.addWater(amount) }
            )
        }
    }
}

@Composable
fun WaterButton(amount: Int, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isPressed) -15f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconRotation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = if (isPressed) 1.dp else 6.dp,
            tonalElevation = 2.dp,
            modifier = Modifier.size(72.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.LocalDrink,
                    contentDescription = null,
                    tint = Color(0xFF1E88E5),
                    modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer {
                            rotationZ = iconRotation
                        }
                )
            }
        }
        Text(
            text = "${amount}ml",
            modifier = Modifier.padding(top = 6.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (isPressed) Color(0xFF1E88E5) else Color(0xFF475569)
        )
    }
}

@Composable
fun CustomWaterButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "customButtonScale"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isPressed) 1.25f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "plusIconScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE3F2FD),
            shadowElevation = if (isPressed) 1.dp else 4.dp,
            modifier = Modifier.size(72.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Boshqa miqdor",
                    tint = Color(0xFF1E88E5),
                    modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        }
                )
            }
        }
        Text(
            text = "Boshqa",
            modifier = Modifier.padding(top = 6.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF1E88E5)
        )
    }
}
