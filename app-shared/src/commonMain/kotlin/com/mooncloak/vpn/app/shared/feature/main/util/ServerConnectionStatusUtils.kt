package com.mooncloak.vpn.app.shared.feature.main.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mooncloak.vpn.app.shared.api.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.theme.ColorPalette

@Composable
internal fun ServerConnectionStatus.floatingActionBarContent(
    modifier: Modifier = Modifier
) {
    when (this) {
        ServerConnectionStatus.Disconnected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.ShieldMoon,
                contentDescription = null,
                tint = LocalContentColor.current
            )
        }

        ServerConnectionStatus.Connecting -> {
            CircularProgressIndicator(
                modifier = modifier,
                color = LocalContentColor.current
            )
        }

        ServerConnectionStatus.Connected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.PowerOff,
                contentDescription = null,
                tint = LocalContentColor.current
            )
        }

        ServerConnectionStatus.Checking -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.ShieldMoon,
                contentDescription = null,
                tint = LocalContentColor.current
            )
        }
    }
}

internal val ServerConnectionStatus.containerColor: Color
    @Composable
    get() = when (this) {
        ServerConnectionStatus.Disconnected -> ColorPalette.MooncloakYellow
        ServerConnectionStatus.Connecting -> MaterialTheme.colorScheme.surface
        ServerConnectionStatus.Connected -> ColorPalette.MooncloakDarkPrimary
        ServerConnectionStatus.Checking -> ColorPalette.MooncloakYellow
    }

internal val ServerConnectionStatus.contentColor: Color
    @Composable
    get() = when (this) {
        ServerConnectionStatus.Disconnected -> ColorPalette.MooncloakDarkPrimaryDark
        ServerConnectionStatus.Connecting -> MaterialTheme.colorScheme.onSurface
        ServerConnectionStatus.Connected -> Color.White
        ServerConnectionStatus.Checking -> ColorPalette.MooncloakDarkPrimaryDark
    }
