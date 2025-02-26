package com.mooncloak.vpn.app.shared.feature.home.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import kotlin.time.Duration

@Immutable
public sealed interface HomeFeedItem {

    @Immutable
    public data class ShowcaseItem public constructor(
        public val title: @Composable () -> String,
        public val description: @Composable () -> String,
        public val icon: @Composable () -> Painter
    ) : HomeFeedItem

    @Immutable
    public data object GetVPNServiceItem : HomeFeedItem

    @Immutable
    public data class PlanUsageItem public constructor(
        public val durationRemaining: String,
        public val bytesRemaining: Long? = null,
        public val showBoost: Boolean = false
    ) : HomeFeedItem

    @Immutable
    public data class AdShieldItem public constructor(
        public val adsBlocked: Int? = null,
        public val trackersBlocked: Int? = null,
        public val estimatedBytesSaved: Long? = null,
        public val active: Boolean = false
    ) : HomeFeedItem

    @Immutable
    public data class ServerConnectionItem public constructor(
        public val connection: VPNConnection.Connected
    ) : HomeFeedItem

    @Immutable
    public data class ServerItem public constructor(
        public val server: Server,
        public val connected: Boolean,
        public val label: String? = null
    ) : HomeFeedItem

    public companion object
}
