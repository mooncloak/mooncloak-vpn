package com.mooncloak.vpn.data.shared.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import kotlin.coroutines.cancellation.CancellationException

/**
 * Interface for a key-value storage system.
 *
 * This interface provides a generic way to interact with storage mechanisms that use string keys
 * to store and retrieve values of various types. It supports asynchronous operations via Kotlin
 * coroutines and uses Kotlin Serialization for handling data serialization and deserialization.
 */
public interface KeyValueStorage {

    /**
     * Checks if the key-value storage contains a value associated with the given key.
     *
     * This function asynchronously determines whether a specific key exists within the storage. It provides a way to
     * query the storage's contents without retrieving the actual value.
     *
     * @param [key] The key to check for existence within the storage.
     *
     * @throws [IllegalArgumentException] if the [key] is blank.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     *
     * @return `true` if the storage contains a value associated with the specified key, `false` otherwise.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun contains(key: String): Boolean

    /**
     * Retrieves a value associated with the given key from the storage.
     *
     * This function attempts to retrieve a stored value, deserialize it using the provided [deserializer], and return
     * the resulting object. If no value is found for the given key or if deserialization fails, it returns `null`.
     *
     * @param [key] The key associated with the desired value.
     *
     * @param [deserializer] The [KSerializer] responsible for deserializing the stored value into the expected type
     * [Value].
     *
     * @throws [IllegalArgumentException] if the [key] is blank.
     *
     * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     *
     * @return The deserialized value if found, otherwise `null`.
     */
    @Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
    public suspend fun <Value : Any> get(
        key: String,
        deserializer: KSerializer<Value>
    ): Value?

    public companion object
}

/**
 *  [MutableKeyValueStorage] is an interface representing a mutable key-value storage. It extends [KeyValueStorage] and
 *  provides functionality to set (write) values associated with keys.
 */
public interface MutableKeyValueStorage : KeyValueStorage {

    /**
     * Sets a value associated with the given key.
     *
     * This function is used to store a value in a persistent storage. It supports serialization using the provided
     * [serializer]. If the [value] is null, the entry associated with the [key] will be removed from the storage.
     *
     * @param [key] The key to associate with the value. Must be a non-empty string.
     *
     * @param [value] The value to store, or null to remove the entry.
     *
     * @param [serializer] The serializer used to serialize and deserialize the value.
     *
     * @param [Value] The type of the value being stored. Must be a non-null type.
     *
     * @throws [IllegalArgumentException] if the [key] is blank.
     *
     * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     */
    @Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
    public suspend fun <Value : Any> set(
        key: String,
        value: Value?,
        serializer: KSerializer<Value>
    )

    /**
     * Removes the value associated with the given [key].
     *
     * If a value is associated with the [key], it will be removed. If no value is associated with the [key], this
     * function does nothing.
     *
     * @param [key] The key associated with the value to be removed.
     *
     * @throws [IllegalArgumentException] if the [key] is blank.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun remove(key: String)

    /**
     * Clears all data associated with this key-value storage.
     *
     * This function should be used to reset the state or remove any stored information that is no longer needed. After
     * calling [clear], the context should be in a state equivalent to when it was first initialized.
     */
    public suspend fun clear()

    public companion object
}

/**
 * [FlowableKeyValueStorage] extends the [KeyValueStorage] interface, providing the capability to observe changes to
 * values associated with specific keys as a Kotlin [Flow].
 *
 * This interface is designed for scenarios where you need to reactively update your application's state based on
 * modifications to stored key-value pairs. It builds upon the basic get/set/remove operations of [KeyValueStorage] by
 * adding a [flow] method that allows for asynchronous, stream-based observation of value changes.
 *
 * Implementations of this interface are expected to emit new values to the flow whenever a change occurs to the value
 * associated with the observed key. This change could be a new value being set, an existing value being updated, or a
 * value being removed.
 */
public interface FlowableKeyValueStorage : KeyValueStorage {

    /**
     * Retrieves a hot [Flow] of [Value]s for the provided [key] that emits whenever a change occurs to the underlying
     * value for that [key]. The resulting [Flow] starts with the current [Value] that is stored for the provided
     * [key].
     *
     * @param [key] The key to associate with the value. Must be a non-empty string.
     *
     * @param [deserializer] The [KSerializer] responsible for deserializing the stored value into the expected type
     * [Value].
     *
     * @throws [IllegalArgumentException] if the [key] is blank.
     *
     * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     *
     * @return A [Flow] of [Value]s that are emitted when changes occur.
     */
    @Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
    public fun <Value : Any> flow(
        key: String,
        deserializer: KSerializer<Value>
    ): Flow<Value?>

    public companion object
}

/**
 * A key-value storage that implements the [KeyValueStorage], [MutableKeyValueStorage], and [FlowableKeyValueStorage]
 * interfaces.
 */
public interface FlowableMutableKeyValueStorage : KeyValueStorage,
    MutableKeyValueStorage,
    FlowableKeyValueStorage {

    public companion object
}

/**
 * Retrieves a value associated with the given key from the storage.
 *
 * This function attempts to retrieve a stored value, deserialize it, and return the resulting object. If no value is
 * found for the given key or if deserialization fails, it returns `null`.
 *
 * This is a convenience function for invoking the [KeyValueStorage.get] function with a deserializer value obtained
 * via the [serializer] inline function.
 *
 * @param [key] The key associated with the desired value.
 *
 * @throws [IllegalArgumentException] if the [key] is blank.
 *
 * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
 *
 * @throws [CancellationException] If the suspending function was cancelled.
 *
 * @return The deserialized value if found, otherwise `null`.
 *
 * @see [KeyValueStorage.get]
 */
@Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
public suspend inline fun <reified Value : Any> KeyValueStorage.get(key: String): Value? = this.get(
    key = key,
    deserializer = serializer()
)

/**
 * Sets a value associated with the given key.
 *
 * This function is used to store a value in a persistent storage. It supports serialization using the obtained
 * [serializer]. If the [value] is null, the entry associated with the [key] will be removed from the storage.
 *
 * This is a convenience function for invoking the [MutableKeyValueStorage.set] function with a serializer value
 * obtained via the [serializer] inline function.
 *
 * @param [key] The key to associate with the value. Must be a non-empty string.
 *
 * @param [value] The value to store, or null to remove the entry.
 *
 * @param [Value] The type of the value being stored. Must be a non-null type.
 *
 * @throws [IllegalArgumentException] if the [key] is blank.
 *
 * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
 *
 * @throws [CancellationException] If the suspending function was cancelled.
 *
 * @see [MutableKeyValueStorage.set]
 */
@Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
public suspend inline fun <reified Value : Any> MutableKeyValueStorage.set(
    key: String,
    value: Value?
) {
    this.set(
        key = key,
        value = value,
        serializer = serializer()
    )
}

/**
 * Retrieves a hot [Flow] of [Value]s for the provided [key] that emits whenever a change occurs to the underlying
 * value for that [key]. The resulting [Flow] starts with the current [Value] that is stored for the provided
 * [key].
 *
 * This is a convenience function for invoking the [FlowableKeyValueStorage.flow] function with a serializer value
 * obtained via the [serializer] inline function.
 *
 * @param [key] The key to associate with the value. Must be a non-empty string.
 *
 * @throws [IllegalArgumentException] if the [key] is blank.
 *
 * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
 *
 * @throws [CancellationException] If the suspending function was cancelled.
 *
 * @return A [Flow] of [Value]s that are emitted when changes occur.
 *
 * @see [FlowableKeyValueStorage.flow]
 */
@Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
public inline fun <reified Value : Any> FlowableKeyValueStorage.flow(
    key: String
): Flow<Value?> = this.flow(
    key = key,
    deserializer = serializer()
)
