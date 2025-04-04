package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import com.mooncloak.vpn.app.shared.theme.LocalThemePreference
import com.mooncloak.vpn.app.shared.theme.MooncloakTheme

/**
 * A Mooncloak styled version of a Dialog.
 */
@Composable
public fun MooncloakDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable () -> Unit
) {
    // For some reason the theme doesn't seem to propagate.
    val themePreference = LocalThemePreference.current

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        MooncloakTheme(
            themePreference = themePreference
        ) {
            Surface(
                modifier = Modifier.sizeIn(minWidth = 300.dp).then(modifier),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(DefaultHorizontalPageSpacing),
                    horizontalAlignment = horizontalAlignment
                ) {
                    content.invoke()
                }
            }
        }
    }
}
