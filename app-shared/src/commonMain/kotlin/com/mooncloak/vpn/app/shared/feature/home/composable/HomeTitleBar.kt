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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.home_title_bar_connecting
import com.mooncloak.vpn.app.shared.resource.home_title_bar_protected
import com.mooncloak.vpn.app.shared.resource.home_title_bar_unprotected
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Immutable
internal enum class HomeTitleBarConnectionStatus {

    Disconnected,
    Connecting,
    Connected
}


@Composable
internal fun HomeTitleBar(
    status: HomeTitleBarConnectionStatus,
    countryName: String,
    ipAddress: String,
    hideIpAddress: Boolean = true,
    modifier: Modifier = Modifier
) {
    val backgroundColor = animateColorAsState(
        targetValue = status.containerColor
    )

    Surface(
        modifier = modifier,
        color = backgroundColor.value
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

            HomeTitleChip(
                modifier = Modifier.wrapContentSize()
                    .padding(top = 12.dp),
                countryName = countryName,
                ipAddress = ipAddress,
                hideIpAddress = true,
                containerColor = status.containerColor,
                contentColor = status.contentColor
            )
        }
    }
}

private val HomeTitleBarConnectionStatus.title: String
    @Composable
    get() = when (this) {
        HomeTitleBarConnectionStatus.Disconnected -> stringResource(Res.string.home_title_bar_unprotected)
        HomeTitleBarConnectionStatus.Connecting -> stringResource(Res.string.home_title_bar_connecting)
        HomeTitleBarConnectionStatus.Connected -> stringResource(Res.string.home_title_bar_protected)
    }

private val HomeTitleBarConnectionStatus.containerColor: Color
    @Composable
    get() = when (this) {
        HomeTitleBarConnectionStatus.Disconnected -> ColorPalette.MooncloakError
        HomeTitleBarConnectionStatus.Connecting -> MaterialTheme.colorScheme.surface
        HomeTitleBarConnectionStatus.Connected -> ColorPalette.Teal_500
    }

@Suppress("SameReturnValue")
private val HomeTitleBarConnectionStatus.contentColor: Color
    @Composable
    get() = when (this) {
        HomeTitleBarConnectionStatus.Disconnected -> Color.White
        HomeTitleBarConnectionStatus.Connecting -> Color.White
        HomeTitleBarConnectionStatus.Connected -> Color.White
    }

@Composable
private fun TitleBarIcon(
    status: HomeTitleBarConnectionStatus,
    modifier: Modifier = Modifier
) {
    when (status) {
        HomeTitleBarConnectionStatus.Disconnected -> {
            Icon(
                modifier = modifier,
                imageVector = Icons.Default.LockOpen,
                contentDescription = null
            )
        }

        HomeTitleBarConnectionStatus.Connecting -> {
            CircularProgressIndicator(
                modifier = modifier
            )
        }

        HomeTitleBarConnectionStatus.Connected -> {
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
