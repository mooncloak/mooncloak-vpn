package com.mooncloak.vpn.api.shared.plan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Currency public constructor(
    @SerialName(value = "type") public val type: Type,
    @SerialName(value = "code") public val code: Code,
    @SerialName(value = "default_fraction_digits") public val defaultFractionDigits: Int? = null,
    @SerialName(value = "numeric_code") public val numericCode: Int? = null,
    @SerialName(value = "symbol") public val symbol: String? = null
) {

    @JvmInline
    @Serializable
    public value class Type public constructor(
        public val value: String
    ) {

        public companion object {

            public val Iso4217: Type = Type(value = "iso4217")
            public val Crypto: Type = Type(value = "crypto")
        }
    }

    @JvmInline
    @Serializable
    public value class Code public constructor(
        public val value: String
    ) {

        public companion object {

            public val USD: Code = Code(value = "USD")
        }
    }
}

public val Currency.USD: Currency get() = usdSingleton

private val usdSingleton = Currency(
    type = Currency.Type.Iso4217,
    code = Currency.Code.USD,
    defaultFractionDigits = 2,
    numericCode = 840,
    symbol = "$"
)
