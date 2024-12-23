package com.mooncloak.vpn.app.shared.api.money

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.jvm.Throws

@Serializable
public sealed interface Currency {

    public val type: String
    public val code: String
    public val defaultFractionDigits: Int?
    public val numericCode: Int?
    public val symbol: String?

    public companion object {

        public const val TYPE_ISO4217: String = "iso4217"
        public const val TYPE_CRYPTO: String = "crypto"
    }
}

@Serializable
@SerialName(value = Currency.TYPE_ISO4217)
public data class Iso4217Currency public constructor(
    @SerialName(value = "code") public override val code: String,
    @SerialName(value = "default_fraction_digits") public override val defaultFractionDigits: Int? = null,
    @SerialName(value = "numeric_code") public override val numericCode: Int? = null,
    @SerialName(value = "symbol") public override val symbol: String? = null
) : Currency {

    @Transient
    public override val type: String = Currency.TYPE_ISO4217
}

@Serializable
@SerialName(value = Currency.TYPE_CRYPTO)
public data class CryptoCurrency public constructor(
    @SerialName(value = "code") public override val code: String,
    @SerialName(value = "default_fraction_digits") public override val defaultFractionDigits: Int? = null,
    @SerialName(value = "numeric_code") public override val numericCode: Int? = null,
    @SerialName(value = "symbol") public override val symbol: String? = null
) : Currency {

    @Transient
    public override val type: String = Currency.TYPE_CRYPTO
}

@Throws(IllegalArgumentException::class)
public operator fun Currency.Companion.invoke(
    type: String,
    code: String,
    defaultFractionDigits: Int? = null,
    numericCode: Int? = null,
    symbol: String? = null
): Currency =
    when (type) {
        TYPE_ISO4217 -> Iso4217Currency(
            code = code,
            defaultFractionDigits = defaultFractionDigits,
            numericCode = numericCode,
            symbol = symbol
        )

        TYPE_CRYPTO -> CryptoCurrency(
            code = code,
            defaultFractionDigits = defaultFractionDigits,
            numericCode = numericCode,
            symbol = symbol
        )

        else -> throw IllegalArgumentException("Cannot create a currency of type '$type'. Must be one of '${TYPE_ISO4217}' or '${TYPE_CRYPTO}'.")
    }

public val Currency.Companion.USD: Iso4217Currency get() = usdSingleton

private val usdSingleton = Iso4217Currency(
    code = "USD",
    defaultFractionDigits = 2,
    numericCode = 840,
    symbol = "$"
)
