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
import com.mooncloak.vpn.app.shared.api.server.VPNConnectionStatus
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_checking
import com.mooncloak.vpn.app.shared.resource.cd_action_connect
import com.mooncloak.vpn.app.shared.resource.cd_action_disconnect
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun VPNConnectionStatus.floatingActionBarContent(
    modifier: Modifier = Modifier
) {
    when (this) {
        VPNConnectionStatus.Disconnected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.ShieldMoon,
                contentDescription = stringResource(Res.string.cd_action_connect),
                tint = LocalContentColor.current
            )
        }

        VPNConnectionStatus.Connecting -> {
            CircularProgressIndicator(
                modifier = modifier,
                color = LocalContentColor.current
            )
        }

        VPNConnectionStatus.Disconnecting -> {
            CircularProgressIndicator(
                modifier = modifier,
                color = LocalContentColor.current
            )
        }

        VPNConnectionStatus.Connected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.PowerOff,
                contentDescription = stringResource(Res.string.cd_action_disconnect),
                tint = LocalContentColor.current
            )
        }

        VPNConnectionStatus.Checking -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.ShieldMoon,
                contentDescription = stringResource(Res.string.cd_action_checking),
                tint = LocalContentColor.current
            )
        }
    }
}

internal val VPNConnectionStatus.containerColor: Color
    @Composable
    get() = when (this) {
        VPNConnectionStatus.Disconnected -> ColorPalette.MooncloakYellow
        VPNConnectionStatus.Connecting -> MaterialTheme.colorScheme.surface
        VPNConnectionStatus.Disconnecting -> MaterialTheme.colorScheme.surface
        VPNConnectionStatus.Connected -> ColorPalette.MooncloakDarkPrimary
        VPNConnectionStatus.Checking -> ColorPalette.MooncloakYellow
    }

internal val VPNConnectionStatus.contentColor: Color
    @Composable
    get() = when (this) {
        VPNConnectionStatus.Disconnected -> ColorPalette.MooncloakDarkPrimaryDark
        VPNConnectionStatus.Connecting -> MaterialTheme.colorScheme.onSurface
        VPNConnectionStatus.Disconnecting -> MaterialTheme.colorScheme.onSurface
        VPNConnectionStatus.Connected -> Color.White
        VPNConnectionStatus.Checking -> ColorPalette.MooncloakDarkPrimaryDark
    }
