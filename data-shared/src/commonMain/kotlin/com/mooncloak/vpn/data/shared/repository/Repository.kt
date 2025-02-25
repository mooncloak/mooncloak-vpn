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
 * A [Repository] that allows for mutable operations on stored values.
 *
 * This interface extends [Repository] and provides methods for inserting, updating, removing, and clearing data,
 * amongst other mutable operations.
 */
public interface MutableRepository<Value : Any> : Repository<Value> {

    /**
     * Inserts the provided value with its corresponding [id] into the underlying storage.
     *
     * @param [id] The unique identifier of the value being inserted.
     *
     * @param [value] The function that returns the value to insert.
     *
     * @throws [IllegalArgumentException] if the provided [id] and the identifier of the constructed [Value] does not
     * match or an item already exists containing that [id].
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The insert [Value]. Note that the implementation may perform additional logic and formatting of values,
     * so this returned item might be different than the provided one, but the returned one represents the value that
     * is actually inserted.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun insert(
        id: String,
        value: () -> Value
    ): Value

    /**
     * Updates the value with the provided [id] with the updated value in the underlying storage.
     *
     * @param [id] The unique identifier of the value being inserted.
     *
     * @param [update] The function that is invoked with the current value and returns the newly updated value.
     *
     * @throws [IllegalArgumentException] if the provided [id] and the identifier of the updated [Value] does not
     * match.
     *
     * @throws [NoSuchElementException] If no item with the provided [id] exists.
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The updated [Value]. Note that the implementation may perform additional logic and formatting of values,
     * so this returned item might be different than the provided one, but the returned one represents the value that
     * is actually updated.
     */
    @Throws(IllegalArgumentException::class, NoSuchElementException::class, CancellationException::class)
    public suspend fun update(
        id: String,
        update: Value.() -> Value
    ): Value

    /**
     * Inserts or updates the provided value with its corresponding [id] into the underlying storage.
     *
     * @param [id] The unique identifier of the value being inserted.
     *
     * @param [insert] The function that is invoked to retrieve the value to be inserted.
     *
     * @param [update] The function that is invoked to update the value that already exists.
     *
     * @throws [IllegalArgumentException] if the provided [id] and the identifier of the constructed [Value] does not
     * match or an item already exists containing that [id].
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The insert [Value]. Note that the implementation may perform additional logic and formatting of values,
     * so this returned item might be different than the provided one, but the returned one represents the value that
     * is actually inserted.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun upsert(
        id: String,
        insert: () -> Value,
        update: Value.() -> Value
    ): Value {
        require(id.isNotBlank()) { "ID must not be blank." }

        val current = this.getOrNull(id = id)

        return if (current == null) {
            this.insert(
                id = id,
                value = insert
            )
        } else {
            this.update(
                id = id,
                update = update
            )
        }
    }

    /**
     * Removes the item containing the provided [id] from the underlying storage.
     *
     * @throws [IllegalArgumentException] if the provided [id] is invalid.
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun remove(id: String)

    /**
     * Removes all items from the underlying storage.
     */
    public suspend fun clear()

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

/**
 * Inserts the provided value with its corresponding [id] into the underlying storage.
 *
 * @param [id] The unique identifier of the value being inserted.
 *
 * @param [value] The value to insert.
 *
 * @throws [IllegalArgumentException] if the provided [id] and the identifier of the constructed [Value] does not
 * match or an item already exists containing that [id].
 *
 * @throws [CancellationException] If the coroutine is cancelled.
 *
 * @return The insert [Value]. Note that the implementation may perform additional logic and formatting of values,
 * so this returned item might be different than the provided one, but the returned one represents the value that
 * is actually inserted.
 */
@Throws(IllegalArgumentException::class, CancellationException::class)
public suspend inline fun <Value : Any> MutableRepository<Value>.insert(
    id: String,
    value: Value
): Value = this.insert(
    id = id,
    value = { value }
)
