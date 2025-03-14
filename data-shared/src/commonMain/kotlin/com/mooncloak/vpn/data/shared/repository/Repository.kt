package com.mooncloak.vpn.data.shared.repository

import kotlin.coroutines.cancellation.CancellationException

/**
 * A generic interface for interacting with a data repository in a read-only manner.
 *
 * This interface defines common operations for managing a collection of [Value] objects, such as checking for
 * existence, retrieving specific or multiple elements, and handling potential errors.
 */
public interface Repository<Value : Any> {

    /**
     * Counts the amount items in the underlying storage.
     *
     * @return The amount of items in the underlying storage.
     */
    public suspend fun count(): Int =
        getAll().size

    /**
     * Checks if an item with the given [id] exists.
     *
     * This function checks if an item with the specified [id] is present.
     *
     * @param [id] The ID of the item to check for.
     *
     * @throws [IllegalArgumentException] If the provided [id] is invalid (e.g., blank).
     *
     * @throws [CancellationException] If the coroutine is cancelled while the check is in progress.
     *
     * @return `true` if an item with the given ID exists, `false` otherwise.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun contains(id: String): Boolean =
        getOrNull(id = id) != null

    /**
     * Retrieves the item with the provided [id], or throws [NoSuchElementException] if no item with the provided [id]
     * exists.
     *
     * @param [id] The unique identifier of the item to retrieve.
     *
     * @throws [IllegalArgumentException] If the provided [id] is invalid (e.g., blank).
     *
     * @throws [NoSuchElementException] If no item with the provided [id] exists.
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The item whose identifier value equals the provided [id].
     */
    @Throws(IllegalArgumentException::class, NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): Value

    /**
     * Retrieves all the items in the underlying storage.
     *
     * @return All items in the underlying storage.
     */
    public suspend fun getAll(): List<Value>

    /**
     * Paginates through all the items in the underlying storage.
     *
     * @param [count] The amount of items to retrieve. Defaults to 25.
     *
     * @param [offset] The item offset. Defaults to zero.
     *
     * @return All items in the underlying storage.
     */
    public suspend fun get(
        count: Int = 25,
        offset: Int = 0
    ): List<Value>

    public companion object
}

/**
 * Retrieves the item with the provided [id], or `null` if no item with the provided [id] exists.
 *
 * @param [id] The unique identifier of the item to retrieve.
 *
 * @throws [IllegalArgumentException] If the provided [id] is invalid (e.g., blank).
 *
 * @throws [CancellationException] If the coroutine is cancelled.
 *
 * @return The item whose identifier value equals the provided [id], or `null` if there is no item with the identifier
 * [id].
 */
@Throws(IllegalArgumentException::class, CancellationException::class)
public suspend fun <Value : Any> Repository<Value>.getOrNull(id: String): Value? = try {
    get(id = id)
} catch (_: NoSuchElementException) {
    null
}
