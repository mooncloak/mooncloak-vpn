package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable

@Immutable
public data class WalletStatDetails public constructor(
    public val dailyChange: WalletStat? = null,
    public val allTimeChange: WalletStat? = null
)
