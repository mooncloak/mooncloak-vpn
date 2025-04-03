package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_create_wallet
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_restore_wallet
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_no_wallet
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_no_wallet
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun NoWalletCard(
    onCreateWallet: () -> Unit,
    onRestoreWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.crypto_wallet_title_no_wallet),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.crypto_wallet_description_no_wallet),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier.sizeIn(minWidth = 200.dp)
                    .weight(1f)
                    .pointerHoverIcon(PointerIcon.Hand),
                onClick = onCreateWallet
            ) {
                Text(
                    text = stringResource(Res.string.crypto_wallet_action_create_wallet)
                )
            }

            Button(
                modifier = Modifier.sizeIn(minWidth = 200.dp)
                    .weight(1f)
                    .pointerHoverIcon(PointerIcon.Hand),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = onRestoreWallet
            ) {
                Text(
                    text = stringResource(Res.string.crypto_wallet_action_restore_wallet)
                )
            }
        }
    }
}
