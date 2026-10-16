package com.abdullojon.waterreminder.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WaveWaterGlass(
    progress: Float,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 260.dp,
    onClick: (() -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1.2f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progressAnim"
    )

    // Splash wave amplitude boost when progress increases
    val splashAnim = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        if (progress > 0f) {
            splashAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
            )
            splashAnim.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val glassScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "glassScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "waveTransition")
    
    val waveOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset1"
    )

    val waveOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset2"
    )

    Canvas(
        modifier = modifier
            .size(sizeDp)
            .graphicsLayer {
                scaleX = glassScale
                scaleY = glassScale
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(12.dp)
    ) {
        val width = size.width
        val height = size.height
        val cornerRadius = 32.dp.toPx()

        // Create Glass Shape Path
        val glassPath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, width, height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )
        }

        // Draw Glass Background Fill
        drawRoundRect(
            color = Color(0xFFE1F5FE).copy(alpha = 0.5f),
            size = Size(width, height),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        // Draw Glass Border Outlines
        drawRoundRect(
            color = Color(0xFFB3E5FC),
            size = Size(width, height),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            style = Stroke(width = 6.dp.toPx())
        )

        // Clip water inside Glass Shape
        clipPath(glassPath) {
            val fillHeight = (height * animatedProgress.coerceAtMost(1f))
            val baseWaterY = height - fillHeight
            val waveAmplitudeBoost = splashAnim.value * 14.dp.toPx()
            val waveAmplitude = 12.dp.toPx() + waveAmplitudeBoost
            val waveFrequency = (2f * PI.toFloat()) / width

            // Back Wave Path (Semi-transparent for depth)
            val backWavePath = Path().apply {
                moveTo(0f, height)
                var x = 0f
                while (x <= width) {
                    val y = baseWaterY + waveAmplitude * sin(waveFrequency * x + waveOffset2)
                    lineTo(x, y)
                    x += 4f
                }
                lineTo(width, height)
                close()
            }

            drawPath(
                path = backWavePath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF81D4FA).copy(alpha = 0.6f), Color(0xFF29B6F6).copy(alpha = 0.6f)),
                    startY = baseWaterY - waveAmplitude,
                    endY = height
                )
            )

            // Front Wave Path
            val frontWavePath = Path().apply {
                moveTo(0f, height)
                var x = 0f
                while (x <= width) {
                    val y = baseWaterY + waveAmplitude * sin(waveFrequency * x + waveOffset1)
                    lineTo(x, y)
                    x += 4f
                }
                lineTo(width, height)
                close()
            }

            drawPath(
                path = frontWavePath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1)),
                    startY = baseWaterY - waveAmplitude,
                    endY = height
                )
            )
        }

        // Glass Shine / Highlight Reflection on left edge
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent)
            ),
            topLeft = Offset(8.dp.toPx(), 8.dp.toPx()),
            size = Size(16.dp.toPx(), height - 16.dp.toPx()),
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
        )
    }
}
