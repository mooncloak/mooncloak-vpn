package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletApi

@Stable
public class CryptoWalletViewModel @Inject public constructor(
    private val cryptoWalletApi: CryptoWalletApi,
    private val vpnServiceApi: VpnServiceApi
) : ViewModel<CryptoWalletStateModel>(initialStateValue = CryptoWalletStateModel()) {

    public fun load() {
        // TODO
    }
}
