package com.mooncloak.vpn.data.shared.provider

/**
 * A provider interface for asynchronously retrieving a value.
 *
 * This interface defines a contract for objects that can provide a value of type [Value].
 *
 * Implementations of this interface should handle the logic required to obtain the value, such as fetching it from a
 * remote source, reading it from a database, or computing it.
 *
 * The `Provider` interface is particularly useful in scenarios involving:
 * - Dependency Injection: where a component needs a value but doesn't need to know how it's created.
 * - Asynchronous operations: where retrieving the value may take some time.
 * - Lazy initialization: where the value is only computed or fetched when it's actually needed.
 *
 * @param Value The type of the value that this provider provides.
 */
public fun interface Provider<Value> {

    /**
     * Retrieves the value.
     *
     * @return The retrieved value of type [Value].
     */
    public suspend fun get(): Value

    public companion object
}
