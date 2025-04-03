package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable

@Immutable
public data class WalletBalance public constructor(
    public val amount: FormattedCurrencyAmount,
    public val localEstimate: FormattedCurrencyAmount? = null
)
