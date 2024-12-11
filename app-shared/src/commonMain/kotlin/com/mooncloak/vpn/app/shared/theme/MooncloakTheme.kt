package com.mooncloak.vpn.app.shared.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
public fun MooncloakTheme(
    themePreference: ThemePreference = ThemePreference.System,
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
            )
        ) {
            content()
        }
    }
}
