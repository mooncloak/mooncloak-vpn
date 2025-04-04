package com.mooncloak.vpn.util.shared.currency

import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.isCrypto
import com.mooncloak.vpn.util.shared.currency.isFiat
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

@ExperimentalLocaleApi
public actual val Currency.Formatter.Companion.Default: Currency.Formatter
    get() = DefaultCurrencyFormatter

@ExperimentalLocaleApi
internal object DefaultCurrencyFormatter : Currency.Formatter {

    override fun format(currency: Currency, amount: Long, locale: Locale): String =
        when {
            currency.isFiat() -> FiatCurrencyFormatter.format(currency = currency, amount = amount, locale = locale)
            currency.isCrypto() -> CryptoCurrencyFormatter.format(currency = currency, amount = amount, locale = locale)
            else -> error("Unsupported currency type '${currency.type}' with code '${currency.code}'.")
        }
}

@ExperimentalLocaleApi
internal object FiatCurrencyFormatter : Currency.Formatter {

    override fun format(currency: Currency, amount: Long, locale: Locale): String {
        val javaCurrency = java.util.Currency.getInstance(currency.code.value)
        val javaLocale = java.util.Locale.forLanguageTag(locale.languageTag.value)

        val formatter = NumberFormat.getCurrencyInstance(javaLocale).apply {
            this.currency = javaCurrency
        }

        val amountInMajorUnits = amount.toBigDecimal()
            .movePointLeft(javaCurrency.defaultFractionDigits)

        return formatter.format(amountInMajorUnits)
    }
}

@ExperimentalLocaleApi
internal object CryptoCurrencyFormatter : Currency.Formatter {

    override fun format(currency: Currency, amount: Long, locale: Locale): String {
        val javaLocale = java.util.Locale.forLanguageTag(locale.languageTag.value)

        // Define precision based on cryptocurrency
        val lowercaseCode = currency.code.value.lowercase()
        val fractionDigits = when {
            currency.defaultFractionDigits != null -> currency.defaultFractionDigits
            lowercaseCode == "btc" -> 8 // Bitcoin: 8 decimal places (satoshis)
            lowercaseCode == "eth" -> 18 // Ethereum: 18 decimal places (wei)
            lowercaseCode == "xrp" -> 6 // Ripple: 6 decimal places (drops)
            else -> 8 // Default for unknown cryptos
        }

        val formatter = NumberFormat.getNumberInstance(javaLocale).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = fractionDigits
        }

        val amountInMajorUnits = amount.toBigDecimal()
            .divide(BigDecimal.TEN.pow(fractionDigits), fractionDigits, RoundingMode.HALF_UP)

        // For simplicity, assume amount is already in major units (e.g., BTC, not satoshis)
        val formattedAmount = formatter.format(amountInMajorUnits)

        return "${currency.code.value} $formattedAmount" // e.g., "BTC 1.23450000"
    }
}
