package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.home_title_bar_checking
import com.mooncloak.vpn.app.shared.resource.home_title_bar_connecting
import com.mooncloak.vpn.app.shared.resource.home_title_bar_description_unprotected
import com.mooncloak.vpn.app.shared.resource.home_title_bar_disconnecting
import com.mooncloak.vpn.app.shared.resource.home_title_bar_protected
import com.mooncloak.vpn.app.shared.resource.home_title_bar_unprotected
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun HomeTitleBar(
    status: VPNConnectionStatus,
    connectedName: String?,
    ipAddress: String?,
    hideIpAddress: Boolean = false,
    publicIpAddress: Boolean = false,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = animateColorAsState(
        targetValue = status.containerColor
    )

    Surface(
        modifier = modifier.hazeEffect(
            state = hazeState,
            style = HazeMaterials.regular(
                containerColor = backgroundColor.value
            )
        ) {
            blurEnabled = true
        },
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = status
            ) { targetStatus ->
                TitleBarIcon(
                    modifier = Modifier.size(36.dp),
                    status = targetStatus
                )
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = status.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = status.contentColor
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            status.description?.let { description ->
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(status.contentColor.copy(alpha = SecondaryAlpha)),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (status.description != null && (!connectedName.isNullOrBlank() || !ipAddress.isNullOrBlank())) {
                HomeTitleChip(
                    modifier = Modifier.sizeIn(minWidth = 200.dp)
                        .wrapContentSize()
                        .padding(top = 12.dp),
                    countryName = connectedName,
                    ipAddress = ipAddress,
                    hideIpAddress = hideIpAddress,
                    publicIpAddress = publicIpAddress,
                    containerColor = Color.Transparent,
                    contentColor = status.contentColor.copy(alpha = SecondaryAlpha)
                )
            }
        }
    }
}

private val VPNConnectionStatus.title: String
    @Composable
    get() = when (this) {
        VPNConnectionStatus.Disconnected -> stringResource(Res.string.home_title_bar_unprotected)
        VPNConnectionStatus.Connecting -> stringResource(Res.string.home_title_bar_connecting)
        VPNConnectionStatus.Disconnecting -> stringResource(Res.string.home_title_bar_disconnecting)
        VPNConnectionStatus.Connected -> stringResource(Res.string.home_title_bar_protected)
        VPNConnectionStatus.Checking -> stringResource(Res.string.home_title_bar_checking)
    }

private val VPNConnectionStatus.description: String?
    @Composable
    get() = when (this) {
        VPNConnectionStatus.Disconnected -> stringResource(Res.string.home_title_bar_description_unprotected)
        else -> null
    }

private val VPNConnectionStatus.containerColor: Color
    @Composable
    get() = when (this) {
        VPNConnectionStatus.Disconnected -> MaterialTheme.colorScheme.errorContainer
        VPNConnectionStatus.Connecting -> MaterialTheme.colorScheme.surface
        VPNConnectionStatus.Disconnecting -> MaterialTheme.colorScheme.surface
        VPNConnectionStatus.Connected -> MaterialTheme.colorScheme.tertiaryContainer
        VPNConnectionStatus.Checking -> MaterialTheme.colorScheme.surface
    }

@Suppress("SameReturnValue")
private val VPNConnectionStatus.contentColor: Color
    @Composable
    get() = when (this) {
        VPNConnectionStatus.Disconnected -> MaterialTheme.colorScheme.onErrorContainer
        VPNConnectionStatus.Connecting -> MaterialTheme.colorScheme.onSurface
        VPNConnectionStatus.Disconnecting -> MaterialTheme.colorScheme.onSurface
        VPNConnectionStatus.Connected -> MaterialTheme.colorScheme.onTertiaryContainer
        VPNConnectionStatus.Checking -> MaterialTheme.colorScheme.onSurface
    }

@Composable
private fun TitleBarIcon(
    status: VPNConnectionStatus,
    modifier: Modifier = Modifier
) {
    when (status) {
        VPNConnectionStatus.Disconnected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.LockOpen,
                contentDescription = null,
                tint = status.contentColor
            )
        }

        VPNConnectionStatus.Checking -> {
            CircularProgressIndicator(
                modifier = modifier
            )
        }

        VPNConnectionStatus.Connecting -> {
            CircularProgressIndicator(
                modifier = modifier
            )
        }

        VPNConnectionStatus.Disconnecting -> {
            CircularProgressIndicator(
                modifier = modifier
            )
        }

        VPNConnectionStatus.Connected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.VpnLock,
                contentDescription = null,
                tint = status.contentColor
            )
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun HomeTitleChip(
    countryName: String?,
    ipAddress: String?,
    hideIpAddress: Boolean,
    publicIpAddress: Boolean,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .border(width = 1.dp, color = contentColor, shape = RoundedCornerShape(6.dp))
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val hasCountryName = !countryName.isNullOrBlank()
                val hasIpAddress = !ipAddress.isNullOrBlank()

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = if (publicIpAddress) {
                        Icons.Default.Public
                    } else {
                        Icons.Default.VpnLock
                    },
                    contentDescription = null,
                    tint = contentColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (hasCountryName) {
                    Text(
                        text = countryName ?: "",
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                if (hasIpAddress) {
                    Text(
                        modifier = Modifier.padding(start = if (hasCountryName) 8.dp else 0.dp),
                        text = if (hideIpAddress) {
                            "*".repeat(ipAddress?.length ?: 0)
                        } else {
                            ipAddress ?: ""
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = contentColor
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
