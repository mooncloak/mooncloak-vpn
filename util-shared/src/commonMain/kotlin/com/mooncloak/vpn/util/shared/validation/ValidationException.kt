package com.mooncloak.vpn.util.shared.validation

/**
 * Represents a validation exception.
 */
public open class ValidationException public constructor(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(
    message,
    cause
)
