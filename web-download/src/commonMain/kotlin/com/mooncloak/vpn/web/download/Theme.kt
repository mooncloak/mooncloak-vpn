package com.mooncloak.vpn.web.download

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

// TODO: Share theme components with main app? Placing the theme components into a shared library module might be
//  useful in the future.

internal data object ColorPalette {

    val Purple_600: Color = Color(0xFF5D5DFF)
    val Teal_500: Color = Color(red = 13, green = 149, blue = 137)
    val MooncloakYellow: Color = Color(0xFFFCCA36)
    val MooncloakLightPrimaryVariant: Color = Color(0xFFEDEDED)
    val MooncloakDarkPrimary: Color = Color(0xFF1C2230)
    val MooncloakDarkPrimaryBright: Color = Color(0xFF273043)
    val MooncloakDarkPrimaryDark: Color = Color(0xFF12161F)
    val MooncloakError: Color = Color(0xFFEB3D3D)
}

internal val mooncloakMoonlightColorScheme = lightColorScheme(
    background = ColorPalette.MooncloakDarkPrimaryDark,
    onBackground = Color.White,
    surface = ColorPalette.MooncloakDarkPrimary,
    surfaceContainer = ColorPalette.MooncloakDarkPrimary,
    surfaceContainerLowest = ColorPalette.MooncloakDarkPrimaryDark,
    surfaceContainerLow = ColorPalette.MooncloakDarkPrimary,
    surfaceContainerHigh = ColorPalette.MooncloakDarkPrimaryBright,
    surfaceContainerHighest = ColorPalette.MooncloakDarkPrimaryBright,
    surfaceVariant = ColorPalette.MooncloakDarkPrimaryBright,
    surfaceDim = ColorPalette.MooncloakDarkPrimaryDark,
    surfaceBright = ColorPalette.MooncloakDarkPrimaryBright,
    onSurface = ColorPalette.MooncloakLightPrimaryVariant,
    onSurfaceVariant = ColorPalette.MooncloakDarkPrimaryDark,
    error = ColorPalette.MooncloakError,
    errorContainer = ColorPalette.MooncloakError,
    onError = Color.White,
    onErrorContainer = Color.White,
    primary = ColorPalette.Purple_600,
    primaryContainer = ColorPalette.Purple_600,
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    secondary = ColorPalette.MooncloakYellow,
    secondaryContainer = ColorPalette.MooncloakYellow,
    onSecondary = ColorPalette.MooncloakDarkPrimaryDark,
    onSecondaryContainer = ColorPalette.MooncloakDarkPrimaryDark,
    tertiary = ColorPalette.Teal_500,
    tertiaryContainer = ColorPalette.Teal_500,
    onTertiary = Color.White,
    onTertiaryContainer = Color.White
)

@Composable
internal fun MooncloakTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = mooncloakMoonlightColorScheme,
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(percent = 25)
        )
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primaryContainer,
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f),
            )
        ) {
            content.invoke()
        }
    }
}
