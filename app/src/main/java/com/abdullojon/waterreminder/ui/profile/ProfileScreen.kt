package com.abdullojon.waterreminder.ui.profile

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = hiltViewModel()
        ProfileScreen(viewModel)
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }

    var sleepStartHour by remember { mutableIntStateOf(22) }
    var sleepStartMin by remember { mutableIntStateOf(0) }
    var sleepEndHour by remember { mutableIntStateOf(7) }
    var sleepEndMin by remember { mutableIntStateOf(0) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE3F2FD), Color.White)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.WaterDrop, null, Modifier.size(50.dp), Color(0xFF2196F3))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Profil Yarating", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))

        Spacer(modifier = Modifier.height(24.dp))

        ProfileTextField(name, { name = it }, "Ismingiz", Icons.Default.Person)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileTextField(weight, { weight = it }, "Vazningiz (kg)", Icons.Default.Scale, KeyboardType.Number)

        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Uyqu vaqti (Eslatma kelmaydi)", modifier = Modifier.align(Alignment.Start).padding(start = 8.dp), fontWeight = FontWeight.Bold)
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TimePickerField(
                label = "Uxlayman",
                hour = sleepStartHour,
                minute = sleepStartMin,
                onTimeSelected = { h, m -> sleepStartHour = h; sleepStartMin = m },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            TimePickerField(
                label = "Uyg'onaman",
                hour = sleepEndHour,
                minute = sleepEndMin,
                onTimeSelected = { h, m -> sleepEndHour = h; sleepEndMin = m },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { 
                val w = weight.toFloatOrNull() ?: 0f
                val start = sleepStartHour * 60 + sleepStartMin
                val end = sleepEndHour * 60 + sleepEndMin
                if (name.isNotEmpty() && w > 0) {
                    viewModel.saveProfile(name, w, start, end)
                }
            },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text("Davom Etish", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun TimePickerField(label: String, hour: Int, minute: Int, onTimeSelected: (Int, Int) -> Unit, modifier: Modifier) {
    val context = LocalContext.current
    val timeString = String.format("%02d:%02d", hour, minute)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = timeString,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = Color(0xFFE2E8F0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
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

@Composable
fun ProfileTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, keyboardType: KeyboardType = KeyboardType.Text) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFF2196F3)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2196F3),
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
