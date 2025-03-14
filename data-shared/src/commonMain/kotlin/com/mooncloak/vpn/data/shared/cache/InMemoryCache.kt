package com.mooncloak.vpn.data.shared.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlin.time.Duration

private data class Entry(
    val key: String,
    val value: String?,
    val expires: Instant?
)

private fun Entry.isValid(instant: Instant): Boolean =
    expires == null || instant < expires

/**
 * An in-memory implementation of the [Cache] interface.
 */
public class InMemoryCache public constructor(
    private val format: StringFormat,
    private val clock: Clock,
    private val expirationAfterWrite: Duration?
) : Cache {

    private val cache = mutableMapOf<String, Entry>()

    private val listeners = mutableMapOf<String, MutableStateFlow<String?>>()

    private val mutex = Mutex(locked = false)
    private val emitMutex = Mutex(locked = false)

    override suspend fun contains(key: String): Boolean {
        require(key.isNotBlank()) { "Key must not be blank." }

        val value = cache[key] ?: return false

        return value.isValid(clock.now())
    }

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
        require(key.isNotBlank()) { "Key must not be blank." }

        val entry = cache[key] ?: return null
        val stored = entry.value ?: return null

        if (!entry.isValid(clock.now())) {
            cache.remove(key)

            return null
        }

        return format.decodeFromString(
            deserializer = deserializer,
            string = stored
        )
    }

    override suspend fun <Value : Any> set(key: String, value: Value?, serializer: KSerializer<Value>) {
        require(key.isNotBlank()) { "Key must not be blank." }

        mutex.withLock {
            if (value == null) {
                cache.remove(key)

                emit(key = key, value = null)
            } else {
                val stored = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                val entry = Entry(
                    key = key,
                    value = stored,
                    expires = expirationAfterWrite?.let { clock.now() + it }
                )

                cache[key] = entry

                emit(key = key, value = stored)
            }
        }
    }

    override suspend fun remove(key: String) {
        require(key.isNotBlank()) { "Key must not be blank." }

        mutex.withLock {
            cache.remove(key)

            emit(key = key, value = null)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            val currentKeys = cache.keys

            cache.clear()

            currentKeys.forEach { key ->
                emit(key = key, value = null)
            }
        }
    }

    override fun <Value : Any> flow(key: String, deserializer: KSerializer<Value>): Flow<Value?> {
        require(key.isNotBlank()) { "Key must not be blank." }

        val flow = listeners[key] ?: MutableStateFlow<String?>(null).apply {
            value = cache[key]?.value
        }

        listeners[key] = flow

        return flow.map { stored ->
            if (stored != null) {
                format.decodeFromString(
                    deserializer = deserializer,
                    string = stored
                )
            } else {
                null
            }
        }
    }

    private suspend fun emit(key: String, value: String?) {
        emitMutex.withLock {
            val listener = listeners[key]

            listener?.value = value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InMemoryCache) return false

        if (format != other.format) return false
        if (cache != other.cache) return false
        if (listeners != other.listeners) return false
        if (mutex != other.mutex) return false

        return emitMutex == other.emitMutex
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + cache.hashCode()
        result = 31 * result + listeners.hashCode()
        result = 31 * result + mutex.hashCode()
        result = 31 * result + emitMutex.hashCode()
        return result
    }

    override fun toString(): String =
        "InMemoryCache(format=$format, storage=$cache, listeners=$listeners, mutex=$mutex, emitMutex=$emitMutex)"
}
