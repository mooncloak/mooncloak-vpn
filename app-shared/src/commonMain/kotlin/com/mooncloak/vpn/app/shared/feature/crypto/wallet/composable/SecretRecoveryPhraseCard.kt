package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_copy_srp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SecretRecoveryPhraseCard(
    phrase: String,
    onCopy: () -> Unit,
    onDownload: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    WalletCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectionContainer(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = phrase,
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis
                )
            }

            TooltipBox(
                text = stringResource(Res.string.crypto_wallet_action_copy_srp),
            ) {
                IconButton(
                    modifier = Modifier.padding(start = 16.dp)
                        .size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    enabled = enabled,
                    onClick = {
                        onCopy.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = stringResource(Res.string.crypto_wallet_action_copy_srp),
                    )
                }
            }

            /* TODO: Support downloading the Secret Recovery Phrase as a secret file
            TooltipBox(
                text = stringResource(Res.string.crypto_wallet_action_download_srp),
            ) {
                IconButton(
                    modifier = Modifier.padding(start = 16.dp)
                        .size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(),
                    enabled = enabled,
                    onClick = {
                        onDownload.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = stringResource(Res.string.crypto_wallet_action_download_srp),
                    )
                }
            }*/
        }
    }
}
