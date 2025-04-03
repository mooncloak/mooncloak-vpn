package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.currency.Currency

@Immutable
public data class FormattedCurrencyAmount public constructor(
    private val amount: Currency.Amount,
    public val formatted: String? = null
) : Currency.Amount by amount
