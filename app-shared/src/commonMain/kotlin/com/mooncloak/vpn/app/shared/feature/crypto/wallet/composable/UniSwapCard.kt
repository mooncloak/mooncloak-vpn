package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.uniswap_trade_title
import com.mooncloak.vpn.app.shared.resource.uniswap_trademark_notice
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun UniSwapCard(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = "https://raw.githubusercontent.com/Uniswap/brand-assets/main/Uniswap%20Brand%20Assets/Uniswap_icon_pink.png", // FIXME: Hardcoded Uniswap brand asset URI.
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 16.dp)
                    .weight(1f),
                text = stringResource(Res.string.uniswap_trade_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.uniswap_trademark_notice),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
        )
    }
}
