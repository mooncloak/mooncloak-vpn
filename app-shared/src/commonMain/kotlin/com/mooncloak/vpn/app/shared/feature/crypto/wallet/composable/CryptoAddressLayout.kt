package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_copy_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_open_wallet_app
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_share_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_scan_qr_code
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.util.LocalAppChooser
import com.mooncloak.vpn.app.shared.util.LocalShareHandler
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CryptoAddressLayout(
    address: String,
    uri: String?,
    onAddressCopied: () -> Unit = {},
    onAddressShared: () -> Unit = {},
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    openWalletVisible: Boolean = false
) {
    val clipboardManager = LocalClipboardManager.current
    val appChooser = LocalAppChooser.current
    val shareHandler = LocalShareHandler.current
    val containerQrBrush = QrBrush.solid(MaterialTheme.colorScheme.surface)
    val contentQrBrush = QrBrush.solid(MaterialTheme.colorScheme.onSurface)

    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Box(
            modifier = Modifier.sizeIn(maxWidth = 300.dp, minHeight = 300.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = rememberQrCodePainter(
                    data = uri ?: address
                ) {
                    // TODO: Customize the QR code style
                    colors {
                        dark = contentQrBrush
                        light = containerQrBrush
                        ball = contentQrBrush
                        frame = contentQrBrush
                    }
                },
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )

            Text(
                modifier = Modifier.padding(start = 8.dp)
                    .alignByBaseline(),
                text = stringResource(Res.string.crypto_wallet_label_scan_qr_code),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier.padding(top = 16.dp)
                .sizeIn(maxWidth = 400.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    onClick = {
                        clipboardManager.setText(AnnotatedString(address))
                        onAddressCopied.invoke()
                    }
                ) {
                    TooltipBox(
                        text = stringResource(Res.string.crypto_wallet_action_copy_address)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(Res.string.crypto_wallet_action_copy_address)
                        )
                    }
                }

                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                    onClick = {
                        shareHandler.share(address)
                        onAddressShared.invoke()
                    }
                ) {
                    TooltipBox(
                        text = stringResource(Res.string.crypto_wallet_action_share_address)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(Res.string.crypto_wallet_action_share_address)
                        )
                    }
                }
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
                        text = address,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (openWalletVisible) {
            Button(
                modifier = Modifier.padding(top = 32.dp)
                    .sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .pointerHoverIcon(PointerIcon.Hand),
                onClick = {
                    uri?.let { appChooser.openUri(it) }
                },
                enabled = !uri.isNullOrBlank()
            ) {
                Text(
                    text = stringResource(Res.string.crypto_wallet_action_open_wallet_app)
                )
            }
        }
    }
}
