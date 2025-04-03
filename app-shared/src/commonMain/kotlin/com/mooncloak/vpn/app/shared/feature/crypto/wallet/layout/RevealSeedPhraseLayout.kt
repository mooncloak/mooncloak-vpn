package com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_reveal
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RevealSeedPhraseLayout(
    sheetState: ManagedModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.crypto_wallet_title_reveal)
        ) {

        }
    }
}
