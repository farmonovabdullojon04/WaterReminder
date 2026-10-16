package com.abdullojon.waterreminder.ui.settings

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
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

    var name by remember(userData.name) { mutableStateOf(userData.name) }
    var weightText by remember(userData.weight) {
        mutableStateOf(if (userData.weight > 0) userData.weight.toString() else "")
    }

    var sleepStartHour by remember(userData.sleepStartMin) { mutableIntStateOf(userData.sleepStartMin / 60) }
    var sleepStartMin by remember(userData.sleepStartMin) { mutableIntStateOf(userData.sleepStartMin % 60) }
    var sleepEndHour by remember(userData.sleepEndMin) { mutableIntStateOf(userData.sleepEndMin / 60) }
    var sleepEndMin by remember(userData.sleepEndMin) { mutableIntStateOf(userData.sleepEndMin % 60) }

    var showResetDialog by remember { mutableStateOf(false) }
    var showSuccessToast by remember { mutableStateOf(false) }

    val calculatedGoal = ((weightText.toFloatOrNull() ?: 0f) * 35).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sozlamalar va Profil", fontWeight = FontWeight.Bold) },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Profil ma'lumotlari",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    if (calculatedGoal > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Hisoblangan kunlik norma: $calculatedGoal ml",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E88E5)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Uyqu rejasi (Eslatma kelmaydigan vaqt)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.width(12.dp))

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
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    val w = weightText.toFloatOrNull() ?: 0f
                    val start = sleepStartHour * 60 + sleepStartMin
                    val end = sleepEndHour * 60 + sleepEndMin
                    if (name.isNotEmpty() && w > 0) {
                        viewModel.updateProfile(name, w, start, end)
                        showSuccessToast = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("O'zgarishlarni Saqlash", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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

    if (showSuccessToast) {
        AlertDialog(
            onDismissRequest = { showSuccessToast = false },
            title = { Text("Saqlandi") },
            text = { Text("Profil ma'lumotlaringiz muvaffaqiyatli yangilandi.") },
            confirmButton = {
                TextButton(onClick = { showSuccessToast = false }) {
                    Text("OK")
                }
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
