package com.mooncloak.vpn.api.shared.plan

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public data class Currency public constructor(
    @SerialName(value = "type") public val type: Type,
    @SerialName(value = "code") public val code: Code,
    @SerialName(value = "default_fraction_digits") public val defaultFractionDigits: Int? = null,
    @SerialName(value = "numeric_code") public val numericCode: Int? = null,
    @SerialName(value = "symbol") public val symbol: String? = null,
    @SerialName(value = "name") public val name: String? = null,
    @SerialName(value = "ticker") public val ticker: String? = null,
    @SerialName(value = "chain_id") public val chainId: Long? = null
) {

    @Serializable
    @JvmInline
    public value class Type public constructor(
        public val value: String
    ) {

        public companion object {

            public val Iso4217: Type = Type(value = "iso4217")
            public val Crypto: Type = Type(value = "crypto")
        }
    }

    @Serializable
    @JvmInline
    public value class Code public constructor(
        public val value: String
    ) {

        public companion object {

            public val USD: Code = Code(value = "USD")
            public val Matic: Code = Code(value = "MATIC")
            public val Pol: Code = Code(value = "POL")
            public val Lunaris: Code = Code(value = "LNRS")
            public val Ethereum: Code = Code(value = "ETH")
            public val Bitcoin: Code = Code(value = "BTC")
            public val Monero: Code = Code(value = "XMR")
            public val USDC: Code = Code(value = "USDC")
            public val Tether: Code = Code(value = "USDT")
            public val USDCPolygon: Code = Code(value = "USDC-POL")
            public val TetherPolygon: Code = Code(value = "USDT-POL")
        }
    }

    public interface Formatter {

        @OptIn(ExperimentalLocaleApi::class)
        public fun format(
            currency: Currency,
            amount: Long,
            locale: Locale = Locale.getDefault()
        ): String

        public companion object
    }

    @Serializable
    public enum class Unit(
        public val serialName: String
    ) {

        @SerialName(value = "minor")
        Minor(serialName = "minor"),

        @SerialName(value = "major")
        Major(serialName = "major");

        public companion object {

            public operator fun get(serialName: String): Unit? =
                Unit.entries.firstOrNull { it.serialName.equals(serialName, ignoreCase = true) }
        }
    }

    public companion object
}

public typealias MinorUnits = Long
public typealias MajorUnits = BigDecimal

public val Currency.Companion.USD: Currency get() = usdSingleton

private val usdSingleton = Currency(
    type = Currency.Type.Iso4217,
    code = Currency.Code.USD,
    defaultFractionDigits = 2,
    numericCode = 840,
    symbol = "$"
)
