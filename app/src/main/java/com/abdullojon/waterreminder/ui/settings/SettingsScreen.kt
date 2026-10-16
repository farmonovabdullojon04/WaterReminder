package com.abdullojon.waterreminder.ui.settings

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.abdullojon.waterreminder.data.local.datastore.UserData
import java.util.Locale

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: SettingsViewModel = hiltViewModel()
        SettingsScreen(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val userData by viewModel.userData.collectAsState()

    var showEditModal by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val startStr = String.format(Locale.getDefault(), "%02d:%02d", userData.sleepStartMin / 60, userData.sleepStartMin % 60)
    val endStr = String.format(Locale.getDefault(), "%02d:%02d", userData.sleepEndMin / 60, userData.sleepEndMin % 60)

    val intervalLabel = when (userData.reminderIntervalMin) {
        30 -> "Har 30 daqiqada"
        45 -> "Har 45 daqiqada"
        60 -> "Har 1 soatda"
        90 -> "Har 1.5 soatda"
        120 -> "Har 2 soatda"
        180 -> "Har 3 soatda"
        else -> "Har ${userData.reminderIntervalMin} daqiqada"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mening Profilim va Sozlamalar", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Profile Overview Card with Pencil Edit Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    // Top Right Pencil Edit Button
                    IconButton(
                        onClick = { showEditModal = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(Color(0xFFE3F2FD))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Tahrirlash",
                            tint = Color(0xFF1E88E5)
                        )
                    }

                    Column(horizontalAlignment = Alignment.Start) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE3F2FD)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = Color(0xFF1E88E5),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = if (userData.name.isNotEmpty()) userData.name else "Foydalanuvchi",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF101828)
                                )
                                Text(
                                    text = "Vazni: ${userData.weight.toInt()} kg",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Kunlik Norma", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "${userData.dailyGoal} ml",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E88E5)
                                )
                            }
                            Column {
                                Text("Eslatma Oralig'i", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = intervalLabel,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF101828)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sleep Schedule Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bedtime, null, tint = Color(0xFF1E88E5))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Uyqu Rejasi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Eslatmalar o'chiriladigan vaqt", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Text(
                        text = "$startStr — $endStr",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E88E5),
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Edit Profile Button (Pencil Icon)
            Button(
                onClick = { showEditModal = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profil va Sozlamalarni Tahrirlash", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935))
            ) {
                Text("Ma'lumotlarni tozalash va qayta boshlash", fontSize = 14.sp)
            }
        }
    }

    // Modal Dialog for Editing Profile
    if (showEditModal) {
        EditProfileModalDialog(
            userData = userData,
            onDismiss = { showEditModal = false },
            onSave = { name, weight, sleepStart, sleepEnd, intervalMin ->
                viewModel.updateProfile(name, weight, sleepStart, sleepEnd, intervalMin)
                showEditModal = false
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Qayta boshlash") },
            text = { Text("Barcha suv ichish tarixi hamda profil ma'lumotlaringiz o'chib ketadi. Bunga rozimisiz?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Tozalash")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Bekor qilish")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileModalDialog(
    userData: UserData,
    onDismiss: () -> Unit,
    onSave: (String, Float, Int, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf(userData.name) }
    var weightText by remember { mutableStateOf(if (userData.weight > 0) userData.weight.toString() else "") }

    var sleepStartHour by remember { mutableIntStateOf(userData.sleepStartMin / 60) }
    var sleepStartMin by remember { mutableIntStateOf(userData.sleepStartMin % 60) }
    var sleepEndHour by remember { mutableIntStateOf(userData.sleepEndMin / 60) }
    var sleepEndMin by remember { mutableIntStateOf(userData.sleepEndMin % 60) }

    var selectedInterval by remember { mutableIntStateOf(userData.reminderIntervalMin) }

    val intervalOptions = listOf(
        30 to "30 daq",
        45 to "45 daq",
        60 to "1 soat",
        90 to "1.5 soat",
        120 to "2 soat",
        180 to "3 soat"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF2196F3))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profilni Tahrirlash", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ismingiz") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF2196F3)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Vazningiz (kg)") },
                    leadingIcon = { Icon(Icons.Default.Scale, null, tint = Color(0xFF2196F3)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Eslatma oralig'i (Chastotasi):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    intervalOptions.take(3).forEach { (min, label) ->
                        FilterChip(
                            selected = selectedInterval == min,
                            onClick = { selectedInterval = min },
                            label = { Text(label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    intervalOptions.drop(3).forEach { (min, label) ->
                        FilterChip(
                            selected = selectedInterval == min,
                            onClick = { selectedInterval = min },
                            label = { Text(label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Uyqu vaqti (Eslatmasiz vaqt):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SettingsTimePickerField(
                        label = "Uxlayman",
                        icon = Icons.Default.Bedtime,
                        hour = sleepStartHour,
                        minute = sleepStartMin,
                        onTimeSelected = { h, m -> sleepStartHour = h; sleepStartMin = m },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SettingsTimePickerField(
                        label = "Uyg'onaman",
                        icon = Icons.Default.WbSunny,
                        hour = sleepEndHour,
                        minute = sleepEndMin,
                        onTimeSelected = { h, m -> sleepEndHour = h; sleepEndMin = m },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val w = weightText.toFloatOrNull() ?: 0f
                    val start = sleepStartHour * 60 + sleepStartMin
                    val end = sleepEndHour * 60 + sleepEndMin
                    if (name.isNotEmpty() && w > 0) {
                        onSave(name, w, start, end, selectedInterval)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Saqlash", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bekor qilish", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun SettingsTimePickerField(
    label: String,
    icon: ImageVector,
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = timeString,
            onValueChange = {},
            label = { Text(label) },
            leadingIcon = { Icon(icon, null, tint = Color(0xFF2196F3)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    TimePickerDialog(context, { _, h, m -> onTimeSelected(h, m) }, hour, minute, true).show()
                }
        )
    }
}
