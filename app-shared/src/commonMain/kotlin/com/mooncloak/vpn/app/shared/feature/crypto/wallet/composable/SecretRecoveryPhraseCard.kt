package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_copy_srp
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_hide
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_show
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_hidden_phrase
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_srp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SecretRecoveryPhraseCard(
    phrase: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    onCopy: () -> Unit,
    onDownload: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.sizeIn(maxWidth = 400.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.crypto_wallet_label_srp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    onClick = onToggleVisibility
                ) {
                    TooltipBox(
                        text = if (visible) {
                            stringResource(Res.string.crypto_wallet_action_hide)
                        } else {
                            stringResource(Res.string.crypto_wallet_action_show)
                        }
                    ) {
                        Icon(
                            imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (visible) {
                                stringResource(Res.string.crypto_wallet_action_hide)
                            } else {
                                stringResource(Res.string.crypto_wallet_action_show)
                            }
                        )
                    }
                }

                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    enabled = visible,
                    onClick = onCopy
                ) {
                    TooltipBox(
                        text = stringResource(Res.string.crypto_wallet_action_copy_srp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(Res.string.crypto_wallet_action_copy_srp)
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

            WalletCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                SelectionContainer(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (visible) {
                            phrase
                        } else {
                            stringResource(Res.string.crypto_wallet_hidden_phrase)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
