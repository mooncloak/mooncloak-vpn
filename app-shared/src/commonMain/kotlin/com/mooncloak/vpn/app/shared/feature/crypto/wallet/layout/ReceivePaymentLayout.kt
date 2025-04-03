package com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.CryptoAddressLayout
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_receive
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_receive
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReceivePaymentLayout(
    address: String,
    uri: String,
    sheetState: ManagedModalBottomSheetState,
    onAddressCopied: () -> Unit = {},
    onAddressShared: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.crypto_wallet_title_receive),
            description = stringResource(Res.string.crypto_wallet_description_receive),
            titleSpacing = 16.dp
        ) {
            CryptoAddressLayout(
                modifier = Modifier.padding(top = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                address = address,
                uri = uri,
                onAddressCopied = onAddressCopied,
                onAddressShared = onAddressShared,
                openWalletVisible = true
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
