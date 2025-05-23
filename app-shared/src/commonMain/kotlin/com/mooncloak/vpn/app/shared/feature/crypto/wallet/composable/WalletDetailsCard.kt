package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.DetailRow
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_open_polygon_scan
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_amount
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_blockchain
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_estimated_value
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_last_updated
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_network
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_token
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_token_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_token_protocol
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_token_ticker
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_wallet_address
import com.mooncloak.vpn.app.shared.resource.global_not_available
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun WalletDetailsCard(
    blockchain: String?,
    network: String?,
    tokenName: String?,
    tokenTicker: String?,
    tokenAddress: String?,
    walletAddress: String?,
    protocol: String?,
    amount: String?,
    estimatedValue: String?,
    lastUpdated: String?,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    WalletCard(modifier = modifier) {
        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_blockchain),
            value = blockchain ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_network),
            value = network ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_token),
            value = tokenName ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_token_ticker),
            value = tokenTicker ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_token_protocol),
            value = protocol ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_token_address),
            value = tokenAddress ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_wallet_address),
            value = walletAddress ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_amount),
            value = amount ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_estimated_value),
            value = estimatedValue ?: stringResource(Res.string.global_not_available)
        )

        DetailRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(Res.string.crypto_wallet_label_last_updated),
            value = lastUpdated ?: stringResource(Res.string.global_not_available)
        )

        if (tokenAddress != null) {
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    // TODO: Fix hardcoded PolygonScan URI.
                    uriHandler.openUri("https://polygonscan.com/token/$tokenAddress")
                }
            ) {
                Text(
                    text = stringResource(Res.string.crypto_wallet_action_open_polygon_scan)
                )
            }
        }
    }
}
