package com.mooncloak.vpn.app.shared.modifier

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.times
import com.mooncloak.vpn.app.shared.composable.PI
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
public fun Modifier.mooncloakAnimatedGradient(): Modifier =
    this.composed {
        val infiniteTransition = rememberInfiniteTransition()
        val phase = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 10000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        // TODO: Use remember + derivedStateOf
        // Calculate subtle offsets using sine/cosine for natural movement
        val xOffset = (sin(2 * PI * phase.value) * 0.1f) + 0.5f // Oscillates around 0.5
        val yOffset = (cos(2 * PI * phase.value) * 0.1f) + 0.5f // Offset by 90° from x

        // Second set of offsets with a slight phase shift for variety
        val xOffset2 = (sin(2 * PI * (phase.value + 0.25f)) * 0.1f) + 0.5f
        val yOffset2 = (cos(2 * PI * (phase.value + 0.25f)) * 0.1f) + 0.5f

        val points = listOf(
            listOf(
                Point(
                    offset = Offset(x = 0f, y = 0f),
                    color = ColorPalette.Purple_600
                ),
                Point(
                    offset = Offset(x = xOffset, y = 0f),
                    color = ColorPalette.MooncloakYellow
                ),
                Point(
                    offset = Offset(x = 1f, y = 0f),
                    color = ColorPalette.MooncloakYellow
                )
            ),
            listOf(
                Point(
                    offset = Offset(x = 0f, y = 0.2f),
                    color = ColorPalette.MooncloakDarkPrimary // New row, left: Purple
                ),
                Point(
                    offset = Offset(x = xOffset2, y = yOffset2),
                    color = ColorPalette.Purple_600 // New row, middle: Yellow
                ),
                Point(
                    offset = Offset(x = 1f, y = 0.2f),
                    color = ColorPalette.MooncloakYellow // New row, right: Dark
                )
            ),
            listOf(
                Point(
                    offset = Offset(x = 0f, y = 0.4f),
                    color = ColorPalette.MooncloakDarkPrimary // New row, left: Purple
                ),
                Point(
                    offset = Offset(x = xOffset2, y = 0.4f),
                    color = ColorPalette.Purple_600 // New row, middle: Yellow
                ),
                Point(
                    offset = Offset(x = 1f, y = 0.4f),
                    color = ColorPalette.MooncloakYellow // New row, right: Dark
                )
            ),
            listOf(
                Point(
                    offset = Offset(x = 0f, y = 0.6f),
                    color = ColorPalette.MooncloakDarkPrimary // New row, left: Purple
                ),
                Point(
                    offset = Offset(x = xOffset2, y = 0.6f),
                    color = ColorPalette.Purple_600 // New row, middle: Yellow
                ),
                Point(
                    offset = Offset(x = 1f, y = 0.6f),
                    color = ColorPalette.MooncloakYellow // New row, right: Dark
                )
            ),
            listOf(
                Point(
                    offset = Offset(x = 0f, y = 0.8f),
                    color = ColorPalette.MooncloakDarkPrimary
                ),
                Point(
                    offset = Offset(x = xOffset, y = 0.8f),
                    color = ColorPalette.Purple_600
                ),
                Point(
                    offset = Offset(x = 1f, y = 0.8f),
                    color = ColorPalette.MooncloakYellow
                )
            ),
            listOf(
                Point(
                    offset = Offset(x = 0f, y = 1f),
                    color = ColorPalette.MooncloakDarkPrimary
                ),
                Point(
                    offset = Offset(x = xOffset, y = 1f),
                    color = ColorPalette.MooncloakDarkPrimary
                ),
                Point(
                    offset = Offset(x = 1f, y = 1f),
                    color = ColorPalette.Purple_600
                )
            )
        )

        this.meshGradient(
            points = points,
            resolutionX = 5,
            resolutionY = 5,
            blurLevel = 1
        )
    }