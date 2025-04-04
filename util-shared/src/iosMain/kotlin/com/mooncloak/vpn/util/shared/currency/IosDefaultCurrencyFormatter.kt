package com.mooncloak.vpn.util.shared.currency

import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
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

    override fun format(amount: Currency.Amount, locale: Locale): String =
        when {
            amount.currency.isFiat() -> FiatCurrencyFormatter.format(amount, locale)
            amount.currency.isCrypto() -> CryptoCurrencyFormatter.format(amount, locale)
            else -> error("Unsupported currency type '${amount.currency.type}' with code '${amount.currency.code}'.")
        }
}

@ExperimentalLocaleApi
internal object FiatCurrencyFormatter : Currency.Formatter {

    override fun format(amount: Currency.Amount, locale: Locale): String {
        val nsLocale = NSLocale(locale.languageTag.value)
        val formatter = NSNumberFormatter().apply {
            setNumberStyle(NSNumberFormatterCurrencyStyle)
            setLocale(nsLocale)
            setCurrencyCode(amount.currency.code.value)
        }

        // Convert amount to major units (e.g., cents to dollars)
        val fractionDigits = amount.currency.defaultFractionDigits ?: 2 // Default to 2 if null
        val amountInMajorUnits = amount.toMajorUnits().doubleValue() / 10.0.pow(fractionDigits.toDouble())

        return formatter.stringFromNumber(NSNumber(amountInMajorUnits)) ?: "$amountInMajorUnits"
    }
}

@ExperimentalLocaleApi
internal object CryptoCurrencyFormatter : Currency.Formatter {

    override fun format(amount: Currency.Amount, locale: Locale): String {
        val nsLocale = NSLocale(locale.languageTag.value)

        // Define precision based on cryptocurrency
        val lowercaseCode = amount.currency.code.value.lowercase()
        val fractionDigits = when {
            amount.currency.defaultFractionDigits != null -> amount.currency.defaultFractionDigits!!
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
        val amountInMajorUnits = amount.toMajorUnits().doubleValue() / 10.0.pow(fractionDigits.toDouble())

        val formattedAmount = formatter.stringFromNumber(NSNumber(amountInMajorUnits)) ?: "$amountInMajorUnits"
        return "${amount.currency.code.value} $formattedAmount" // e.g., "LNRS 1.23450000"
    }
}
