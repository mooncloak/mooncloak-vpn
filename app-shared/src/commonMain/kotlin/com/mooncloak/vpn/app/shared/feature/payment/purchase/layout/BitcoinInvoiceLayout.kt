package com.mooncloak.vpn.app.shared.feature.payment.purchase.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_bitcoin_address_label
import com.mooncloak.vpn.app.shared.resource.payment_qr_scan_label
import com.mooncloak.vpn.app.shared.resource.payment_status_label
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BitcoinInvoiceLayout(
    uri: String,
    address: String?,
    paymentStatusTitle: String,
    paymentStatusDescription: String?,
    paymentStatusPending: Boolean,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val containerQrBrush = QrBrush.solid(MaterialTheme.colorScheme.surface)
        val contentQrBrush = QrBrush.solid(MaterialTheme.colorScheme.onSurface)

        Image(
            modifier = Modifier.sizeIn(maxWidth = 300.dp)
                .fillMaxWidth()
                .padding(top = 16.dp),
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
            contentDescription = "Payment QR code",
            contentScale = ContentScale.FillWidth
        )

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
                text = stringResource(Res.string.payment_qr_scan_label),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        if (address != null) {
            Text(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.Start)
                    .padding(top = 32.dp),
                text = stringResource(Res.string.payment_bitcoin_address_label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
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
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {
                        clipboardManager.setText(AnnotatedString(address))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Bitcoin Address"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.wrapContentSize()
                .align(Alignment.Start)
                .padding(top = 32.dp),
            text = stringResource(Res.string.payment_status_label),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = paymentStatusTitle,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            paymentStatusDescription?.let { description ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                )
            }

            if (paymentStatusPending) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            onClick = {}
        ) {
            Text(text = "Open Wallet")
        }
    }
}
