package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable

@Immutable
public data class WalletStat public constructor(
    public val value: Double,
    public val formatted: String
)
