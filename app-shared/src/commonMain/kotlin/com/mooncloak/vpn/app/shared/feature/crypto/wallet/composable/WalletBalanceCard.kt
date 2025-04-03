package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_balance
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_balance_zero
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun WalletBalanceCard(
    cryptoAmount: String?,
    localEstimatedAmount: String?,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.crypto_wallet_label_balance),
            style = MaterialTheme.typography.labelMedium,
            color = LocalContentColor.current.copy(alpha = SecondaryAlpha)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cryptoAmount ?: stringResource(Res.string.crypto_wallet_value_balance_zero),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            if (!localEstimatedAmount.isNullOrBlank()) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = localEstimatedAmount,
                    style = MaterialTheme.typography.titleMedium,
                    color = LocalContentColor.current.copy(alpha = SecondaryAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
