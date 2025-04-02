package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class CryptoWalletViewModel @Inject public constructor(

) : ViewModel<CryptoWalletStateModel>(initialStateValue = CryptoWalletStateModel()) {

    public fun load() {
        // TODO
    }
}
