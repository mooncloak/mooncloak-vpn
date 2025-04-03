package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet

@Immutable
public data class CryptoWalletStateModel public constructor(
    public val cryptoWallet: CryptoWallet? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: NotificationStateModel? = null,
    public val successMessage: NotificationStateModel? = null
)

public val CryptoWalletStateModel.sendEnabled: Boolean
    inline get() = this.cryptoWallet != null

public val CryptoWalletStateModel.receiveEnabled: Boolean
    inline get() = this.cryptoWallet != null

public val CryptoWalletStateModel.revealEnabled: Boolean
    inline get() = this.cryptoWallet != null
