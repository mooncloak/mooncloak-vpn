package com.mooncloak.vpn.app.shared.util

import com.mooncloak.kodetools.konstruct.annotations.Inject

public class IPv4AddressValidator @Inject public constructor() {

    public fun validate(value: String): Result<String> {
        if (value.isBlank()) return Result.failure(IllegalArgumentException("IPv4 address must not be blank."))

        val formatted = value.trim()

        return if (formatted.matches(Regex)) {
            Result.success(formatted)
        } else {
            Result.failure(IllegalArgumentException("Invalid IPv4 address '$value'."))
        }
    }

    public companion object {

        private const val PATTERN: String =
            "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"

        private val Regex = Regex(PATTERN)
    }
}
