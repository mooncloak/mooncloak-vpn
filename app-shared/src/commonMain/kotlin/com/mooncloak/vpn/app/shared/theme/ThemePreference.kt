package com.mooncloak.vpn.app.shared.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    SystemDefault(serialName = "system_default");

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
    @ReadOnlyComposable
    get() = when (this) {
        ThemePreference.Dark -> "Dark" // TODO: Resources.strings.settingsTitleThemeDark
        ThemePreference.Light -> "Light" // TODO: Resources.strings.settingsTitleThemeLight
        ThemePreference.SystemDefault -> "System" // TODO: Resources.strings.settingsTitleThemeSystem
    }

public val ThemePreference.icon: ImageVector
    get() = when (this) {
        ThemePreference.Dark -> Icons.Default.DarkMode
        ThemePreference.Light -> Icons.Default.LightMode
        ThemePreference.SystemDefault -> Icons.Default.SettingsBrightness
    }

@Composable
@ReadOnlyComposable
public inline fun ThemePreference.isInDarkTheme(): Boolean =
    when (this) {
        ThemePreference.Dark -> true
        ThemePreference.Light -> false
        ThemePreference.SystemDefault -> androidx.compose.foundation.isSystemInDarkTheme()
    }
