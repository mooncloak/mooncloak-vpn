package com.mooncloak.vpn.data.shared.source

import com.mooncloak.vpn.data.shared.repository.MutableRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class InMemorySource<Value : Any> public constructor(
    initialValues: Map<String, Value> = emptyMap()
) : MutableRepository<Value> {

    private val storage = mutableMapOf<String, Value>().apply {
        putAll(initialValues)
    }

    private val mutex = Mutex(locked = false)

    override suspend fun count(): Int =
        storage.size

    override suspend fun contains(id: String): Boolean =
        storage.contains(id)

    override suspend fun get(id: String): Value =
        storage[id] ?: throw NoSuchElementException("No item found for the provided id '$id'.")

    override suspend fun get(count: Int, offset: Int): List<Value> =
        storage.values.toList().drop(offset).take(count)

    override suspend fun getAll(): List<Value> =
        storage.values.toList()

    override suspend fun insert(id: String, value: () -> Value): Value =
        mutex.withLock {
            val item = value.invoke()

            storage[id] = item

            item
        }

    override suspend fun update(id: String, update: Value.() -> Value): Value =
        mutex.withLock {
            val current = storage[id] ?: throw NoSuchElementException("No item found for the provided id '$id'.")

            val updated = current.update()

            storage[id] = updated

            updated
        }

    override suspend fun remove(id: String) {
        mutex.withLock {
            storage.remove(id)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            storage.clear()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InMemorySource<*>) return false

        if (storage != other.storage) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = storage.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String = "InMemorySource(storage=$storage, mutex=$mutex)"
}
