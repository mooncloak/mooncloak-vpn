package com.mooncloak.vpn.app.shared.feature.payment.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_qr_scan_label
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BitcoinInvoiceLayout(
    uri: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val containerQrBrush = QrBrush.solid(MaterialTheme.colorScheme.surface)
        val contentQrBrush = QrBrush.solid(MaterialTheme.colorScheme.onSurface)

        Image(
            modifier = Modifier.fillMaxWidth()
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
    }
}
