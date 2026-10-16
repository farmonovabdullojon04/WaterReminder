package com.abdullojon.waterreminder.ui.result

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoalResultScreen(
    userName: String,
    isAchieved: Boolean,
    onGoToHome: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (userName.isNotEmpty()) "Hi, $userName," else "Hi,",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF90A4AE),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Large Emoji Face
                if (isAchieved) {
                    HappyEmojiFace(modifier = Modifier.size(110.dp))
                } else {
                    SadEmojiFace(modifier = Modifier.size(110.dp))
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Light Blue Main Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Top Right Party Popper / Confetti Icon
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(20.dp)
                                .size(48.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp)
                        ) {
                            Text(
                                text = if (isAchieved) "Congratulations!" else "Oops!",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF101828)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (isAchieved) {
                                    "$userName, siz bugun o'z maqsadingizga erishdingiz!"
                                } else {
                                    "$userName, bugun kunlik normangizni bajara olmadingiz."
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D2939)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (isAchieved) {
                                    "Boshqalarga o'zi xohlagan narsaga erishishda yordam bersangiz, hayotda xohlagan hamma narsangizga erisha olasiz."
                                } else {
                                    "Muvaffaqiyat oxirgi nuqta emas, mag'lubiyat esa fojia emas: Davom etish jasorati muhimdir."
                                },
                                fontSize = 14.sp,
                                color = Color(0xFF475569),
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Golden Trophy Icon at bottom left
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(72.dp)
                            )
                        }
                    }
                }
            }

            // Bottom "Go to Home" Button
            Button(
                onClick = onGoToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "Bosh sahifaga qaytish",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun HappyEmojiFace(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Yellow background
        drawCircle(
            color = Color(0xFFFFCA28),
            radius = radius,
            center = center
        )

        // Black outline
        drawCircle(
            color = Color(0xFF212121),
            radius = radius,
            center = center,
            style = Stroke(width = 6.dp.toPx())
        )

        // Left Eye (big round pupil)
        drawCircle(
            color = Color(0xFF212121),
            radius = radius * 0.14f,
            center = Offset(center.x - radius * 0.35f, center.y - radius * 0.2f)
        )

        // Right Eye
        drawCircle(
            color = Color(0xFF212121),
            radius = radius * 0.14f,
            center = Offset(center.x + radius * 0.35f, center.y - radius * 0.2f)
        )

        // Big Grinning Mouth (Open Smile)
        val mouthPath = Path().apply {
            moveTo(center.x - radius * 0.45f, center.y + radius * 0.1f)
            cubicTo(
                center.x - radius * 0.45f, center.y + radius * 0.75f,
                center.x + radius * 0.45f, center.y + radius * 0.75f,
                center.x + radius * 0.45f, center.y + radius * 0.1f
            )
            close()
        }

        drawPath(
            path = mouthPath,
            color = Color(0xFF212121)
        )

        // Tongue inside mouth
        val tonguePath = Path().apply {
            moveTo(center.x - radius * 0.25f, center.y + radius * 0.35f)
            cubicTo(
                center.x - radius * 0.25f, center.y + radius * 0.65f,
                center.x + radius * 0.25f, center.y + radius * 0.65f,
                center.x + radius * 0.25f, center.y + radius * 0.35f
            )
            close()
        }

        drawPath(
            path = tonguePath,
            color = Color(0xFFE53935)
        )
    }
}

@Composable
fun SadEmojiFace(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Yellow background
        drawCircle(
            color = Color(0xFFFFCA28),
            radius = radius,
            center = center
        )

        // Black outline
        drawCircle(
            color = Color(0xFF212121),
            radius = radius,
            center = center,
            style = Stroke(width = 6.dp.toPx())
        )

        // Left Eye
        drawCircle(
            color = Color(0xFF212121),
            radius = radius * 0.13f,
            center = Offset(center.x - radius * 0.35f, center.y - radius * 0.15f)
        )

        // Right Eye
        drawCircle(
            color = Color(0xFF212121),
            radius = radius * 0.13f,
            center = Offset(center.x + radius * 0.35f, center.y - radius * 0.15f)
        )

        // Eyebrows slanted down (sad expression)
        drawLine(
            color = Color(0xFF212121),
            start = Offset(center.x - radius * 0.45f, center.y - radius * 0.4f),
            end = Offset(center.x - radius * 0.2f, center.y - radius * 0.3f),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = Color(0xFF212121),
            start = Offset(center.x + radius * 0.45f, center.y - radius * 0.4f),
            end = Offset(center.x + radius * 0.2f, center.y - radius * 0.3f),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Sad Downturned Mouth
        val mouthPath = Path().apply {
            moveTo(center.x - radius * 0.35f, center.y + radius * 0.4f)
            cubicTo(
                center.x - radius * 0.2f, center.y + radius * 0.15f,
                center.x + radius * 0.2f, center.y + radius * 0.15f,
                center.x + radius * 0.35f, center.y + radius * 0.4f
            )
        }

        drawPath(
            path = mouthPath,
            color = Color(0xFF212121),
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
