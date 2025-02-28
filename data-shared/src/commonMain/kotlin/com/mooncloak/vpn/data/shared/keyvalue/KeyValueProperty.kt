package com.mooncloak.vpn.data.shared.keyvalue

import kotlinx.coroutines.flow.Flow

/**
 * Represents a property that holds a value associated with a key.
 *
 * This interface provides a way to retrieve the stored value asynchronously.
 *
 * @param Value The type of the value held by this property.
 */
public interface KeyValueProperty<Value : Any> {

    /**
     * Retrieves a value.
     *
     * This function attempts to retrieve a value.
     *
     * @return The retrieved value if it exists, or `null` if no value is available.
     */
    public suspend fun get(): Value?

    /**
     * Retrieves a [Flow] of the underlying value changes.
     */
    public fun flow(): Flow<Value?>

    public companion object
}

/**
 * Represents a key-value property that can be modified.
 *
 * This interface extends [KeyValueProperty] and provides methods to set and remove the associated value.
 *
 * @param Value The type of the value associated with the property. Must be non-nullable.
 */
public interface MutableKeyValueProperty<Value : Any> : KeyValueProperty<Value> {

    /**
     * Sets the current value.
     *
     * This function allows updating the stored value asynchronously.
     *
     * @param value The new value to set. Can be null.
     */
    public suspend fun set(value: Value?)

    /**
     * Removes the current resource or object associated with this instance.
     */
    public suspend fun remove()

    public companion object
}
