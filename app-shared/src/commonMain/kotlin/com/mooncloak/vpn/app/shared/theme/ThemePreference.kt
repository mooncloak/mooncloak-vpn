package com.mooncloak.vpn.app.shared.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.preference_theme_dark
import com.mooncloak.vpn.app.shared.resource.preference_theme_light
import com.mooncloak.vpn.app.shared.resource.preference_theme_system
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Immutable
@Serializable
public enum class ThemePreference(
    public val serialName: String
) {

    @SerialName(value = "dark")
    Dark(serialName = "dark"),

    @SerialName(value = "light")
    Light(serialName = "light"),

    @SerialName(value = "system_default")
    System(serialName = "system_default");

    public companion object {

        public operator fun get(serialName: String): ThemePreference? =
            entries.firstOrNull { preference ->
                preference.serialName.equals(
                    serialName,
                    ignoreCase = true
                )
            }
    }
}

public val ThemePreference.title: String
    @Composable
    get() = when (this) {
        ThemePreference.Dark -> stringResource(Res.string.preference_theme_dark)
        ThemePreference.Light -> stringResource(Res.string.preference_theme_light)
        ThemePreference.System -> stringResource(Res.string.preference_theme_system)
    }

public val ThemePreference.icon: ImageVector
    get() = when (this) {
        ThemePreference.Dark -> Icons.Default.DarkMode
        ThemePreference.Light -> Icons.Default.LightMode
        ThemePreference.System -> Icons.Default.SettingsBrightness
    }

@Suppress("NOTHING_TO_INLINE")
@Composable
@ReadOnlyComposable
public inline fun ThemePreference.isInDarkTheme(): Boolean =
    when (this) {
        ThemePreference.Dark -> true
        ThemePreference.Light -> false
        ThemePreference.System -> androidx.compose.foundation.isSystemInDarkTheme()
    }

public val LocalThemePreference: ProvidableCompositionLocal<ThemePreference> =
    staticCompositionLocalOf { ThemePreference.System }
