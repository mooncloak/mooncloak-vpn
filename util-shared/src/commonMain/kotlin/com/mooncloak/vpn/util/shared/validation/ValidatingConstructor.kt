package com.mooncloak.vpn.util.shared.validation

/**
 * Represents a [Validator] component that can be used to construct new instances of a type after validation using the
 * [from] function.
 */
public fun interface ValidatingConstructor<T, R> : Validator<T, R> {

    /**
     * Retrieves a [T] instance from the provided [value] if it is valid, otherwise a [ValidationException] is thrown.
     *
     * @param [value] The [T] value to validate.
     *
     * @throws [ValidationException] if the provided [value] was not valid.
     *
     * @return [R]
     */
    @Throws(ValidationException::class)
    public fun from(value: T): R =
        try {
            this.validate(value = value).getOrThrow()
        } catch (e: ValidationException) {
            throw e
        } catch (e: Exception) {
            throw ValidationException(cause = e)
        }

    public companion object
}

/**
 * Retrieves a [T] instance from the provided [value] if it is valid, otherwise `null`.
 *
 * @param [value] The [T] value to validate.
 *
 * @return [R]
 */
public fun <T, R> ValidatingConstructor<T, R>.fromOrNull(value: T): R? =
    try {
        from(value = value)
    } catch (_: ValidationException) {
        null
    }
