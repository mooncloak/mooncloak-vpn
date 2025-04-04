package com.mooncloak.vpn.util.shared.validation

/**
 * A [Validator] performs validation on an input type of [T] and returns a [Result] of type [R].
 * This validation process determines whether the provided input to the [validate] function matches
 * an expected format criteria. If the validation process passes, then a successful [Result] should
 * be returned. Otherwise, a [Result.Failure] should be returned.
 */
public fun interface Validator<T, R> {

    /**
     * A function that determines whether the provided [value] is valid. If the provided [value] is
     * deemed valid, then a successful [Result] object, wrapping the returned type, will be
     * returned. If the provided [value] is deemed invalid, then a [Result.Failure] object,
     * wrapping any ValidationErrors encountered, will be returned.
     */
    public fun validate(value: T): Result<R>

    public companion object
}

/**
 * Determines whether the provided [value] is considered valid.
 *
 * @param [value] The [T] value to check for validity.
 *
 * @return `true` if the value is considered valid, `false` otherwise.
 */
public fun <T, R> Validator<T, R>.isValid(value: T): Boolean =
    this.validate(value).isSuccess
