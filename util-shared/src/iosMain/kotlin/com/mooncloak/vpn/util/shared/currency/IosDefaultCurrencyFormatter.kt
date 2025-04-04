package com.mooncloak.vpn.util.shared.currency

import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.isCrypto
import com.mooncloak.vpn.util.shared.currency.isFiat
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import kotlin.math.pow

@ExperimentalLocaleApi
public actual val Currency.Formatter.Companion.Default: Currency.Formatter
    get() = DefaultCurrencyFormatter

@ExperimentalLocaleApi
internal object DefaultCurrencyFormatter : Currency.Formatter {

    override fun format(currency: Currency, amount: Long, locale: Locale): String =
        when {
            currency.isFiat() -> FiatCurrencyFormatter.format(currency, amount, locale)
            currency.isCrypto() -> CryptoCurrencyFormatter.format(currency, amount, locale)
            else -> error("Unsupported currency type '${currency.type}' with code '${currency.code}'.")
        }
}

@ExperimentalLocaleApi
internal object FiatCurrencyFormatter : Currency.Formatter {

    override fun format(currency: Currency, amount: Long, locale: Locale): String {
        val nsLocale = NSLocale(locale.languageTag.value)
        val formatter = NSNumberFormatter().apply {
            setNumberStyle(NSNumberFormatterCurrencyStyle)
            setLocale(nsLocale)
            setCurrencyCode(currency.code.value)
        }

        // Convert amount to major units (e.g., cents to dollars)
        val fractionDigits = currency.defaultFractionDigits ?: 2 // Default to 2 if null
        val amountInMajorUnits = amount.toDouble() / 10.0.pow(fractionDigits.toDouble())

        return formatter.stringFromNumber(NSNumber(amountInMajorUnits)) ?: "$amountInMajorUnits"
    }
}

@ExperimentalLocaleApi
internal object CryptoCurrencyFormatter : Currency.Formatter {

    override fun format(currency: Currency, amount: Long, locale: Locale): String {
        val nsLocale = NSLocale(locale.languageTag.value)

        // Define precision based on cryptocurrency
        val lowercaseCode = currency.code.value.lowercase()
        val fractionDigits = when {
            currency.defaultFractionDigits != null -> currency.defaultFractionDigits
            lowercaseCode == "btc" -> 8 // Bitcoin: 8 decimals (satoshis)
            lowercaseCode == "eth" -> 18 // Ethereum: 18 decimals (wei)
            lowercaseCode == "xrp" -> 6 // Ripple: 6 decimals (drops)
            lowercaseCode == "lnrs" -> 18 // Lunaris on Polygon: 18 decimals
            else -> 8 // Default for unknown cryptos
        }

        val formatter = NSNumberFormatter().apply {
            setNumberStyle(NSNumberFormatterDecimalStyle)
            setLocale(nsLocale)
            setMinimumFractionDigits(2u)
            setMaximumFractionDigits(fractionDigits.toULong())
        }

        // Convert amount to major units (e.g., wei-like to LNRS)
        val amountInMajorUnits = amount.toDouble() / 10.0.pow(fractionDigits.toDouble())

        val formattedAmount = formatter.stringFromNumber(NSNumber(amountInMajorUnits)) ?: "$amountInMajorUnits"
        return "${currency.code.value} $formattedAmount" // e.g., "LNRS 1.23450000"
    }
}
