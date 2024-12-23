package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.settings_group_theme
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import org.jetbrains.compose.resources.stringResource

@Suppress("UnusedReceiverParameter")
@Composable
internal fun ColumnScope.SettingsThemeGroup(
    themePreference: ThemePreference?,
    onThemePreferenceValueChanged: (preference: ThemePreference) -> Unit
) {
    SettingsGroupLabel(
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(top = 32.dp),
        text = stringResource(Res.string.settings_group_theme)
    )

    ThemePreferenceSegmentedButton(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 16.dp, bottom = 16.dp),
        themePreference = themePreference ?: ThemePreference.System,
        onThemePreferenceSelected = onThemePreferenceValueChanged
    )
}
