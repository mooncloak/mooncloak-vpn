package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_account_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_account_address
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountAddressCard(
    address: String,
    uri: String?,
    onAddressCopied: () -> Unit = {},
    onAddressShared: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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

        CryptoAddressLayout(
            modifier = Modifier.padding(top = 24.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            address = address,
            uri = uri,
            onAddressCopied = onAddressCopied,
            onAddressShared = onAddressShared
        )
    }
}
