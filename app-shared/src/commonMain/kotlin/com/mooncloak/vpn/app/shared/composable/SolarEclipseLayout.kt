package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun SolarEclipseLayout(
    modifier: Modifier = Modifier,
    containerColor: Color = ColorPalette.MooncloakDarkPrimary,
    contentColor: Color = Color.White,
    shape: Shape = CircleShape,
    calculateMoonSize: BoxWithConstraintsScope.() -> DpSize = {
        val min = minOf(maxWidth, maxHeight)

        DpSize(width = min * 0.6f, height = min * 0.6f)
    },
    calculateSunSize: BoxWithConstraintsScope.(moonSize: DpSize) -> DpSize = { size -> size * 1.3f },
    multiplier: Float = 1f,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.sizeIn(
            minWidth = 300.dp,
            minHeight = 300.dp
        ).then(modifier),
        contentAlignment = Alignment.Center
    ) {
        val moonSize = remember(this) { calculateMoonSize.invoke(this) }
        val sunSize = remember(moonSize) { calculateSunSize.invoke(this, moonSize) }

        Box(
            modifier = Modifier.size(sunSize)
                .blur(
                    radius = 15.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
        ) {
            CircleWave(
                modifier = Modifier.matchParentSize(),
                clockwise = true,
                speed = multiplier * 1f / 1000f,
                frequency = 7,
                shift = multiplier * TWO_PI / 3f
            )

            CircleWave(
                modifier = Modifier.matchParentSize(),
                clockwise = false,
                startDelay = 300.milliseconds,
                speed = multiplier * 1.1f / 1000f,
                frequency = 5,
                shift = multiplier * TWO_PI / 2f,
                alpha = 0.7f
            )

            CircleWave(
                modifier = Modifier.matchParentSize(),
                clockwise = true,
                startDelay = 1.5.seconds,
                speed = multiplier * 1.4f / 1000f,
                frequency = 3,
                shift = multiplier * TWO_PI / 4f,
                alpha = 0.5f
            )

            CircleWave(
                modifier = Modifier.matchParentSize(),
                clockwise = false,
                startDelay = 2.seconds,
                speed = multiplier * 0.2f / 1000f,
                frequency = 1,
                shift = multiplier * TWO_PI,
                alpha = 0.2f
            )
        }

        Box(
            modifier = Modifier.size(moonSize)
                .shadow(elevation = 8.dp, shape = shape)
                .clip(shape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content.invoke(this)
            }
        }
    }
}
