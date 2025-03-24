package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.mooncloak.vpn.app.shared.feature.app.AppDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_destination_home
import com.mooncloak.vpn.app.shared.resource.cd_destination_servers
import com.mooncloak.vpn.app.shared.resource.cd_destination_settings
import com.mooncloak.vpn.app.shared.resource.cd_destination_support
import com.mooncloak.vpn.app.shared.resource.destination_main_home_title
import com.mooncloak.vpn.app.shared.resource.destination_main_servers_title
import com.mooncloak.vpn.app.shared.resource.destination_main_settings_title
import com.mooncloak.vpn.app.shared.resource.destination_main_support_title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Immutable
@Serializable
public sealed interface MainDestination : AppDestination {

    public val primary: Boolean

    @Serializable
    @SerialName(value = "home")
    @Immutable
    public data object Home : MainDestination {

        override val path: String = "/home"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_home_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.Home)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_home)

        override val primary: Boolean = true
    }

    @Serializable
    @SerialName(value = "servers")
    @Immutable
    public data object Servers : MainDestination {

        override val path: String = "/servers"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_servers_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.VpnLock)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_servers)

        override val primary: Boolean = true
    }

    @Serializable
    @SerialName(value = "support")
    @Immutable
    public data object Support : MainDestination {

        override val path: String = "/support"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_support_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.SupportAgent)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_support)

        override val primary: Boolean = true
    }

    @Serializable
    @SerialName(value = "settings")
    @Immutable
    public data object Settings : MainDestination {

        override val path: String = "/settings"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_settings_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.Settings)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_settings)

        override val primary: Boolean = true
    }
}
