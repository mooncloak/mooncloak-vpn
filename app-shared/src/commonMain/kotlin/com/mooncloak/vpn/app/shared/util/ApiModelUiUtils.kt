package com.mooncloak.vpn.app.shared.util

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.mooncloak.vpn.app.shared.api.ConnectionType
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.connection_type_multiple
import com.mooncloak.vpn.app.shared.resource.connection_type_p2p
import com.mooncloak.vpn.app.shared.resource.connection_type_tor
import org.jetbrains.compose.resources.stringResource

internal val ConnectionType.title: String
    @Composable
    get() = when (this) {
        ConnectionType.P2P -> stringResource(Res.string.connection_type_p2p)
        ConnectionType.Tor -> stringResource(Res.string.connection_type_tor)
        ConnectionType.MultipleVpn -> stringResource(Res.string.connection_type_multiple)
        else -> this.value
    }

internal val ConnectionType.icon: Painter?
    @Composable
    get() = when (this) { // TODO: ConnectionType icons
        ConnectionType.P2P -> null
        ConnectionType.Tor -> null
        ConnectionType.MultipleVpn -> null
        else -> null
    }
