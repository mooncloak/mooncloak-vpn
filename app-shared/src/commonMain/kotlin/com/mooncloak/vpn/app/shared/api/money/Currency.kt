package com.mooncloak.vpn.app.shared.api.money

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Currency public constructor(
    @SerialName(value = "type") public val type: CurrencyType,
    @SerialName(value = "code") public val code: String,
    @SerialName(value = "default_fraction_digits") public val defaultFractionDigits: Int? = null,
    @SerialName(value = "numeric_code") public val numericCode: Int? = null,
    @SerialName(value = "symbol") public val symbol: String? = null
)

@Immutable
@Serializable
@JvmInline
public value class CurrencyType public constructor(
    public val value: String
) {

    public companion object {

        public val Iso4217: CurrencyType = CurrencyType(value = "iso4217")
        public val Crypto: CurrencyType = CurrencyType(value = "crypto")
    }
}
