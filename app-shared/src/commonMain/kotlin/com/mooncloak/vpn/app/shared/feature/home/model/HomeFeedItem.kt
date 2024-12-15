package com.mooncloak.vpn.app.shared.feature.home.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.mooncloak.vpn.app.shared.api.server.ConnectionType
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import com.mooncloak.vpn.app.shared.api.server.Server
import kotlinx.datetime.Instant
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
        public val durationRemaining: Duration,
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
        public val country: Country,
        public val region: Region? = null,
        public val server: Server,
        public val connectionType: ConnectionType,
        public val connected: Boolean = false,
        public val default: Boolean = false,
        public val starred: Boolean = false,
        public val lastUsed: Instant? = null
    ) : HomeFeedItem

    public companion object
}
