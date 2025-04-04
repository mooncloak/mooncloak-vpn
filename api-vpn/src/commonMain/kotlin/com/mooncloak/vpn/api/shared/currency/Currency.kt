package com.mooncloak.vpn.api.shared.currency

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.serialization.kotlinx.bigdecimal.BigDecimalHumanReadableSerializer
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
    @SerialName(value = "chain_id") public val chainId: Long? = null,
    @SerialName(value = "address") public val address: String? = null
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

    @Serializable(with = CurrencyAmountSerializer::class)
    public interface Amount {

        public val currency: Currency
        public val unit: Unit
        public val value: BigDecimal

        public fun toMinorUnits(): MinorUnits
        public fun toMajorUnits(): MajorUnits

        public companion object
    }

    public companion object
}

public typealias MinorUnits = Long
public typealias MajorUnits = BigDecimal

public expect val Currency.Formatter.Companion.Default: Currency.Formatter

public fun Currency.isFiat(): Boolean =
    this.type == Currency.Type.Iso4217

public fun Currency.isCrypto(): Boolean =
    this.type == Currency.Type.Crypto

public val Currency.Companion.USD: Currency get() = usdSingleton

public val Currency.Companion.Lunaris: Currency get() = lunarisSingleton

// Pol and Matic are the same. Pol replaced Matic, so we support both the currency codes.
public val Currency.Companion.Matic: Currency get() = maticSingleton
public val Currency.Companion.Pol: Currency get() = polSingleton

private val usdSingleton = Currency(
    type = Currency.Type.Iso4217,
    code = Currency.Code.USD,
    defaultFractionDigits = 2,
    numericCode = 840,
    symbol = "$"
)

private val lunarisSingleton = Currency(
    type = Currency.Type.Crypto,
    code = Currency.Code.Lunaris,
    defaultFractionDigits = 18,
    numericCode = null,
    symbol = "L",
    name = "Lunaris",
    ticker = "LNRS",
    chainId = 137L,
    address = "0x23C2d7673Dd36FF6cBD642A5a70f58c1D2118C13"
)

private val maticSingleton = Currency(
    type = Currency.Type.Crypto,
    code = Currency.Code.Matic,
    defaultFractionDigits = 18,
    numericCode = null,
    symbol = "M",
    name = "Matic",
    ticker = "MATIC",
    chainId = 137L
)
private val polSingleton = Currency(
    type = Currency.Type.Crypto,
    code = Currency.Code.Pol,
    defaultFractionDigits = 18,
    numericCode = null,
    symbol = "M",
    name = "Pol",
    ticker = "POL",
    chainId = 137L
)

@Serializable
private data class CurrencyAmountDelegate(
    @SerialName(value = "currency") val currency: Currency,
    @SerialName(value = "unit") val unit: Currency.Unit,
    @SerialName(value = "value") @Serializable(with = BigDecimalHumanReadableSerializer::class) val value: BigDecimal
)

internal object CurrencyAmountSerializer : KSerializer<Currency.Amount> {

    override val descriptor: SerialDescriptor = CurrencyAmountDelegate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Currency.Amount) {
        val delegate = CurrencyAmountDelegate(
            currency = value.currency,
            unit = value.unit,
            value = value.value
        )

        encoder.encodeSerializableValue(
            serializer = CurrencyAmountDelegate.serializer(),
            value = delegate
        )
    }

    override fun deserialize(decoder: Decoder): Currency.Amount {
        val delegate = decoder.decodeSerializableValue(deserializer = CurrencyAmountDelegate.serializer())

        return Currency.Amount(
            currency = delegate.currency,
            unit = delegate.unit,
            value = delegate.value
        )
    }
}
