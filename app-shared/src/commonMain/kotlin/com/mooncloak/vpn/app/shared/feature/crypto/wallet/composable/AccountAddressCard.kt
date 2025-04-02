package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_copy_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_account_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_account_address
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountAddressCard(
    address: String,
    uri: String,
    onAddressCopied: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val containerQrBrush = QrBrush.solid(MaterialTheme.colorScheme.surface)
    val contentQrBrush = QrBrush.solid(MaterialTheme.colorScheme.onSurface)

    WalletCard(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.crypto_wallet_title_account_address),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(Res.string.crypto_wallet_description_account_address),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
        )

        Box(
            modifier = Modifier.padding(top = 24.dp)
                .sizeIn(maxWidth = 300.dp, minHeight = 300.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = rememberQrCodePainter(
                    data = uri
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
            modifier = Modifier.padding(top = 16.dp)
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(5.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = address,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            IconButton(
                modifier = Modifier.padding(start = 16.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
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
        }
    }
}
