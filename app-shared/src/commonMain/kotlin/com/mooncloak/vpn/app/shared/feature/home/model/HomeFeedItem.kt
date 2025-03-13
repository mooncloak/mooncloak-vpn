package com.mooncloak.vpn.app.shared.feature.home.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.vpn.VPNConnection

@Immutable
public sealed interface HomeFeedItem {

    public val id: String
    public val contentType: String

    @Immutable
    public data class ShowcaseItem public constructor(
        public override val id: String,
        public val title: @Composable () -> String,
        public val description: @Composable () -> String,
        public val icon: @Composable () -> Painter
    ) : HomeFeedItem {

        override val contentType: String = "ShowcaseItem"
    }

    @Immutable
    public data object GetVPNServiceItem : HomeFeedItem {

        override val id: String = "GetVPNServiceItem"

        override val contentType: String = "GetVPNServiceItem"
    }

    @Immutable
    public data class PlanUsageItem public constructor(
        public val durationRemaining: String,
        public val bytesRemaining: Long? = null,
        public val showBoost: Boolean = false
    ) : HomeFeedItem {

        override val id: String = "PlanUsageItem"

        override val contentType: String = "PlanUsageItem"
    }

    @Immutable
    public data class MoonShieldItem public constructor(
        public val trackersBlocked: String? = null,
        public val estimatedBytesSaved: String? = null,
        public val estimatedTimeSaved: String? = null,
        public val active: Boolean = false,
        public val toggleEnabled: Boolean = false
    ) : HomeFeedItem {

        override val id: String = "MoonShieldItem"

        override val contentType: String = "MoonShieldItem"
    }

    @Immutable
    public data class ServerConnectionItem public constructor(
        public val connection: VPNConnection.Connected
    ) : HomeFeedItem {

        override val id: String = "ConnectedServer"

        override val contentType: String = "ConnectedServer"
    }

    @Immutable
    public data class ServerItem public constructor(
        public val server: Server,
        public val connected: Boolean,
        public val label: String? = null
    ) : HomeFeedItem {

        override val id: String = server.id

        override val contentType: String = "ServerItem"
    }

    public companion object
}
