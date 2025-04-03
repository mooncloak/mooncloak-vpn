package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_receive
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_refresh
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_reveal
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_send
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun WalletActions(
    onSend: () -> Unit,
    onReceive: () -> Unit,
    onReveal: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    sendEnabled: Boolean = true,
    receiveEnabled: Boolean = true,
    revealEnabled: Boolean = true,
    refreshEnabled: Boolean = true,
    sendVisible: Boolean = true,
    receiveVisible: Boolean = true,
    revealVisible: Boolean = true,
    refreshVisible: Boolean = true
) {
    Box(modifier = modifier) {
        FlowRow(
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WalletAction(
                icon = Icons.AutoMirrored.Default.CallMade,
                contentDescription = stringResource(Res.string.crypto_wallet_action_send),
                visible = sendVisible,
                enabled = sendEnabled,
                onAction = onSend
            )

            WalletAction(
                icon = Icons.AutoMirrored.Default.CallReceived,
                contentDescription = stringResource(Res.string.crypto_wallet_action_receive),
                visible = receiveVisible,
                enabled = receiveEnabled,
                onAction = onReceive
            )

            WalletAction(
                icon = Icons.Default.Visibility,
                contentDescription = stringResource(Res.string.crypto_wallet_action_reveal),
                visible = revealVisible,
                enabled = revealEnabled,
                onAction = onReveal
            )

            WalletAction(
                icon = Icons.Default.Refresh,
                contentDescription = stringResource(Res.string.crypto_wallet_action_refresh),
                visible = refreshVisible,
                enabled = refreshEnabled,
                onAction = onRefresh
            )
        }
    }
}

@Composable
private fun RowScope.WalletAction(
    icon: ImageVector,
    contentDescription: String,
    visible: Boolean,
    enabled: Boolean,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TooltipBox(
            text = contentDescription
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    modifier = Modifier.size(48.dp)
                        .pointerHoverIcon(PointerIcon.Hand),
                    onClick = onAction,
                    enabled = enabled,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = contentDescription,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
