package com.mooncloak.vpn.app.shared.composable

// The following code was adapted from the "jetpack-loading" project under the Apache 2.0 License:
// https://github.com/MahboubehSeyedpour/jetpack-loading
// https://github.com/MahboubehSeyedpour/jetpack-loading#Apache-2.0-1-ov-file
// Which, in turn was adapted from the AVLoadingIndicatorView library under the Apache 2.0 License:
// https://github.com/HarlonWang/AVLoadingIndicatorView

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
public fun PulsatingDots(
    modifier: Modifier = Modifier,
    count: Int = 3,
    color: Color = MaterialTheme.colorScheme.onBackground,
    ballDiameter: Float = 40f,
    horizontalSpace: Float = 20f,
    animationDuration: Int = 600,
    minAlpha: Float = 0f,
    maxAlpha: Float = 1f
) {
    val scales: List<Float> = (0 until count).map { index ->
        var scale by remember { mutableStateOf(maxAlpha) }

        LaunchedEffect(key1 = Unit) {
            delay(animationDuration / count * index.toLong())

            animate(
                initialValue = minAlpha,
                targetValue = maxAlpha,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDuration,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
            ) { value, _ ->
                scale = value
            }
        }

        scale
    }

    Canvas(modifier = modifier) {
        for (index in 0 until count) {
            val xOffset = ballDiameter + horizontalSpace

            drawCircle(
                color = color,
                radius = (ballDiameter / 2) * scales[index],
                center = Offset(
                    x = when {
                        index < count / 2 -> -(center.x + xOffset)
                        index == count / 2 -> center.x
                        else -> center.x + xOffset
                    },
                    y = 0f
                )
            )
        }
    }
}
