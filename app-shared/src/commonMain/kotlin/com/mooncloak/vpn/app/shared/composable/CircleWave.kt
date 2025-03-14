/*
 * The following code was inspired by the following project, licensed under MIT:
 * https://github.com/alexjlockwood/bees-and-bombs-compose
 * https://github.com/alexjlockwood/bees-and-bombs-compose?tab=MIT-1-ov-file#readme
 */

package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.translate
import com.mooncloak.vpn.app.shared.composable.CircleWaveDefaults.FREQUENCY
import com.mooncloak.vpn.app.shared.composable.CircleWaveDefaults.SHIFT
import com.mooncloak.vpn.app.shared.composable.CircleWaveDefaults.SPEED
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal const val PI = kotlin.math.PI.toFloat()
internal const val TWO_PI = 2 * PI
internal const val N = 360

internal object CircleWaveDefaults {

    internal const val SPEED = 1f / 1000f
    internal const val SHIFT = TWO_PI / 3f
    internal const val FREQUENCY = 8

    internal val colors: List<Color> = listOf(
        ColorPalette.Purple_600,
        ColorPalette.MooncloakError,
        ColorPalette.MooncloakYellow,
    )

    internal val brush: Brush = Brush.radialGradient(
        colorStops = arrayOf(
            0.5f to ColorPalette.Purple_600,
            0.6f to ColorPalette.MooncloakError,
            1f to ColorPalette.MooncloakYellow
        ),
        center = Offset(x = 1f, y = 1f)
    )
}

@Composable
internal fun CircleWave(
    modifier: Modifier = Modifier,
    brushes: List<Brush> = listOf(
        CircleWaveDefaults.brush,
        CircleWaveDefaults.brush,
        CircleWaveDefaults.brush,
    ),
    animate: Boolean = true,
    startDelay: Duration = 0.milliseconds,
    speed: Float = SPEED,
    frequency: Int = FREQUENCY,
    shift: Float = SHIFT,
    clockwise: Boolean = true,
    alpha: Float = 1f,
    drawStyle: DrawStyle = Fill,
    blendMode: BlendMode = BlendMode.Softlight
) {
    val millis by animationTimeMillis(
        enabled = animate,
        startDelay = startDelay
    )
    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val (width, height) = size
        val waveAmplitude = size.minDimension / 20
        val circleRadius = size.minDimension / 2f - 2 * waveAmplitude

        translate(width / 2f, height / 2f) {
            brushes.forEachIndexed { colorIndex, brush ->
                path.reset()
                for (i in 0 until N) {
                    val a = i * TWO_PI / N
                    val t = millis * speed
                    val c = cos(a * frequency - colorIndex * shift + t)
                    val p = ((1 + cos(a - t)) / 2).pow(3)
                    val r = circleRadius + waveAmplitude * c * p
                    val x = r * sin(a)
                    val y = if (clockwise) {
                        r * -cos(a)
                    } else {
                        r * cos(a)
                    }
                    if (i == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
                path.close()

                drawPath(
                    path = path,
                    brush = brush,
                    alpha = alpha,
                    style = drawStyle,
                    blendMode = blendMode,
                )
            }
        }
    }
}

/**
 * Returns a [State] holding a local animation time in milliseconds. The value always starts
 * at `0L` and stops updating when the call leaves the composition.
 */
@Composable
private fun animationTimeMillis(
    enabled: Boolean = true,
    startDelay: Duration = 0.milliseconds
): State<Long> {
    val millisState = remember { mutableStateOf(0L) }
    val animate = remember(enabled) { mutableStateOf(enabled) }

    LaunchedEffect(Unit) {
        delay(startDelay)

        val startTime = withFrameMillis { it }

        while (enabled && animate.value) {
            withFrameMillis { frameTime ->
                millisState.value = frameTime - startTime
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            animate.value = false
        }
    }

    return millisState
}
