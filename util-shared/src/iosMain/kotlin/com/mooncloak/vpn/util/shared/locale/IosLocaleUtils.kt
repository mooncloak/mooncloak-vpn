package com.mooncloak.vpn.util.shared.locale

import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import platform.Foundation.NSLocale
import platform.Foundation.NSNumberFormatter
import platform.Foundation.currentLocale
import platform.Foundation.localeWithLocaleIdentifier

@ExperimentalLocaleApi
public actual val Locale.decimalSeparator: String
    get() {
        val nsLocale = NSLocale.localeWithLocaleIdentifier(languageTag.value)
            ?: NSLocale.currentLocale()

        val formatter = NSNumberFormatter().apply {
            setLocale(nsLocale)
        }

        return formatter.decimalSeparator ?: "."
    }

@ExperimentalLocaleApi
public actual val Locale.groupingSeparator: String
    get() {
        val nsLocale = NSLocale.localeWithLocaleIdentifier(languageTag.value)
            ?: NSLocale.currentLocale()

        val formatter = NSNumberFormatter().apply {
            setLocale(nsLocale)
        }

        return formatter.groupingSeparator ?: ","
    }
