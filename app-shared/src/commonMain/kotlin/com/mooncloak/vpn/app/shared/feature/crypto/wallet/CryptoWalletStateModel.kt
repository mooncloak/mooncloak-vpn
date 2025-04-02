package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.model.NotificationStateModel

@Immutable
public data class CryptoWalletStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: NotificationStateModel? = null,
    public val successMessage: NotificationStateModel? = null
)
