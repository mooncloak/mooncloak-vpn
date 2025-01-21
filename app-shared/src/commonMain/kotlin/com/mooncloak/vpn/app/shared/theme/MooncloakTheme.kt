package com.mooncloak.vpn.app.shared.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

public interface MooncloakTheme {

    public val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    public val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    public val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    public companion object : MooncloakTheme
}

@Composable
public fun MooncloakTheme(
    themePreference: ThemePreference = LocalThemePreference.current,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (themePreference.isInDarkTheme()) {
            mooncloakMoonlightColorScheme
        } else {
            mooncloakDaylightColorScheme
        },
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(percent = 25)
        )
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primaryContainer,
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f),
            ),
            LocalThemePreference provides themePreference
        ) {
            content()
        }
    }
}
