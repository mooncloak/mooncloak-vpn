package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.home_title_bar_checking
import com.mooncloak.vpn.app.shared.resource.home_title_bar_connecting
import com.mooncloak.vpn.app.shared.resource.home_title_bar_protected
import com.mooncloak.vpn.app.shared.resource.home_title_bar_unprotected
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.HazeMaterials
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeTitleBar(
    status: ServerConnectionStatus,
    connectedName: String?,
    ipAddress: String?,
    hideIpAddress: Boolean = true,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = animateColorAsState(
        targetValue = status.containerColor
    )

    Surface(
        modifier = modifier.hazeChild(
            state = hazeState,
            style = HazeMaterials.ultraThin(
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
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!connectedName.isNullOrBlank() || !ipAddress.isNullOrBlank()) {
                HomeTitleChip(
                    modifier = Modifier.wrapContentSize()
                        .padding(top = 12.dp),
                    countryName = connectedName,
                    ipAddress = ipAddress,
                    hideIpAddress = hideIpAddress,
                    containerColor = Color.Transparent,
                    contentColor = status.contentColor
                )
            }
        }
    }
}

private val ServerConnectionStatus.title: String
    @Composable
    get() = when (this) {
        ServerConnectionStatus.Disconnected -> stringResource(Res.string.home_title_bar_unprotected)
        ServerConnectionStatus.Connecting -> stringResource(Res.string.home_title_bar_connecting)
        ServerConnectionStatus.Connected -> stringResource(Res.string.home_title_bar_protected)
        ServerConnectionStatus.Checking -> stringResource(Res.string.home_title_bar_checking)
    }

private val ServerConnectionStatus.containerColor: Color
    @Composable
    get() = when (this) {
        ServerConnectionStatus.Disconnected -> MaterialTheme.colorScheme.errorContainer
        ServerConnectionStatus.Connecting -> MaterialTheme.colorScheme.surface
        ServerConnectionStatus.Connected -> MaterialTheme.colorScheme.tertiaryContainer
        ServerConnectionStatus.Checking -> MaterialTheme.colorScheme.surface
    }

@Suppress("SameReturnValue")
private val ServerConnectionStatus.contentColor: Color
    @Composable
    get() = when (this) {
        ServerConnectionStatus.Disconnected -> MaterialTheme.colorScheme.onErrorContainer
        ServerConnectionStatus.Connecting -> MaterialTheme.colorScheme.onSurface
        ServerConnectionStatus.Connected -> MaterialTheme.colorScheme.onTertiaryContainer
        ServerConnectionStatus.Checking -> MaterialTheme.colorScheme.onSurface
    }

@Composable
private fun TitleBarIcon(
    status: ServerConnectionStatus,
    modifier: Modifier = Modifier
) {
    when (status) {
        ServerConnectionStatus.Disconnected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.LockOpen,
                contentDescription = null
            )
        }

        ServerConnectionStatus.Checking -> {
            CircularProgressIndicator(
                modifier = modifier
            )
        }

        ServerConnectionStatus.Connecting -> {
            CircularProgressIndicator(
                modifier = modifier
            )
        }

        ServerConnectionStatus.Connected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.VpnLock,
                contentDescription = null
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

                if (hasCountryName) {
                    Text(
                        text = countryName ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = SecondaryAlpha
                            )
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
