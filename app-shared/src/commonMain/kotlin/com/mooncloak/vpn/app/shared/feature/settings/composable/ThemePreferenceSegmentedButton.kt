package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.app.shared.theme.icon
import com.mooncloak.vpn.app.shared.theme.title

@Composable
internal fun ThemePreferenceSegmentedButton(
    themePreference: ThemePreference,
    onThemePreferenceSelected: (preference: ThemePreference) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        ThemePreference.entries.forEach { preference ->
            SegmentedButton(
                selected = preference == themePreference,
                onClick = {
                    onThemePreferenceSelected.invoke(preference)
                },
                shape = MaterialTheme.shapes.medium,
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    activeBorderColor = MaterialTheme.colorScheme.outline,
                    inactiveContainerColor = MaterialTheme.colorScheme.background,
                    inactiveContentColor = MaterialTheme.colorScheme.onBackground,
                    inactiveBorderColor = MaterialTheme.colorScheme.outline
                ),
                label = {
                    Text(text = preference.title)
                },
                icon = {
                    Icon(
                        imageVector = preference.icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
