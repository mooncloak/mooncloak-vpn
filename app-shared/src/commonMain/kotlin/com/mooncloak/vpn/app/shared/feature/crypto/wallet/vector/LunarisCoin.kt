package com.mooncloak.vpn.app.shared.feature.crypto.wallet.vector

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.ColorPalette

@Suppress("UnusedReceiverParameter")
public val Icons.Filled.LunarisCoin: ImageVector
    get() {
        iconSingleton?.let { return it }

        val icon = Builder(
            name = "LunarisCoinIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Filled circle for the coin body (radius 10, centered at 12,12)
            path(
                fill = SolidColor(ColorPalette.MooncloakYellowDark),
                fillAlpha = 1.0f,
                stroke = null,
                pathFillType = NonZero
            ) {
                moveTo(12f, 2f) // Start at the top (12, 2)
                curveTo(6.47f, 2f, 2f, 6.47f, 2f, 12f) // Top-left quadrant
                reflectiveCurveToRelative(4.47f, 10f, 10f, 10f) // Bottom-left quadrant
                reflectiveCurveToRelative(10f, -4.47f, 10f, -10f) // Bottom-right quadrant
                reflectiveCurveTo(17.53f, 2f, 12f, 2f) // Top-right quadrant
                close()
            }

            // Border circle (stroke, radius 11, centered at 12,12)
            path(
                fill = null,
                stroke = SolidColor(ColorPalette.MooncloakYellow),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = NonZero
            ) {
                moveTo(12f, 1f) // Start at the top (12, 1)
                curveTo(6.03f, 1f, 1f, 6.03f, 1f, 12f) // Top-left quadrant
                reflectiveCurveToRelative(5.03f, 11f, 11f, 11f) // Bottom-left quadrant
                reflectiveCurveToRelative(11f, -5.03f, 11f, -11f) // Bottom-right quadrant
                reflectiveCurveTo(17.97f, 1f, 12f, 1f) // Top-right quadrant
                close()
            }

            // Crescent moon (white, inside the coin)
            path(
                fill = SolidColor(ColorPalette.MooncloakLightPrimaryVariant),
                fillAlpha = 1.0f,
                stroke = null,
                pathFillType = NonZero
            ) {
                // Adjusted coordinates to center at (12,12) and flip to face left
                moveTo(15.97f, 14.41f)
                curveToRelative(-1.84f, 2.17f, -5.21f, 2.1f, -6.96f, -0.07f)
                curveToRelative(-2.19f, -2.72f, -0.65f, -6.72f, 2.69f, -7.33f)
                curveToRelative(0.34f, -0.06f, 0.63f, 0.27f, 0.51f, 0.6f)
                curveToRelative(-0.46f, 1.23f, -0.39f, 2.64f, 0.32f, 3.86f)
                curveToRelative(0.71f, 1.22f, 1.89f, 1.99f, 3.18f, 2.2f)
                curveTo(16.05f, 13.72f, 16.2f, 14.14f, 15.97f, 14.41f)
                close()
            }
        }.build()

        iconSingleton = icon

        return icon
    }

private var iconSingleton: ImageVector? = null
