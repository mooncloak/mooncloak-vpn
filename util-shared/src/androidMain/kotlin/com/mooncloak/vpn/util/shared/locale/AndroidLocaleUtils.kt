package com.mooncloak.vpn.util.shared.locale

import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import java.text.DecimalFormatSymbols

@ExperimentalLocaleApi
public fun Locale.toJvmLocale(): java.util.Locale =
    java.util.Locale.forLanguageTag(this.languageTag.value)

@ExperimentalLocaleApi
public actual val Locale.decimalSeparator: String
    get() {
        val jvmLocale = this.toJvmLocale()
        val symbols = DecimalFormatSymbols(jvmLocale)

        return symbols.decimalSeparator.toString()
    }

@ExperimentalLocaleApi
public actual val Locale.groupingSeparator: String
    get() {
        val jvmLocale = this.toJvmLocale()
        val symbols = DecimalFormatSymbols(jvmLocale)

        return symbols.groupingSeparator.toString()
    }
