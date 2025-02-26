package com.mooncloak.vpn.data.shared.cache

import androidx.collection.LruCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlin.time.Duration

public actual fun Cache.Companion.create(
    format: StringFormat,
    maxSize: Int?,
    expirationAfterWrite: Duration?,
    clock: Clock
): Cache = AndroidCache(
    format = format,
    cache = LruCache(maxSize ?: Int.MAX_VALUE),
    expirationAfterWrite = expirationAfterWrite,
    clock = clock
)

internal data class Entry internal constructor(
    val key: String,
    val value: String?,
    val expires: Instant?
)

internal fun Entry.isValid(instant: Instant): Boolean =
    expires == null || instant < expires

internal class AndroidCache internal constructor(
    private val format: StringFormat,
    private val cache: LruCache<String, Entry>,
    private val clock: Clock,
    private val expirationAfterWrite: Duration?
) : Cache {

    private val mutex = Mutex(locked = false)

    override suspend fun contains(key: String): Boolean =
        cache[key] != null

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
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

    override fun <Value : Any> flow(key: String, deserializer: KSerializer<Value>): Flow<Value?> {
        TODO("Not yet implemented")
    }

    override suspend fun <Value : Any> set(key: String, value: Value?, serializer: KSerializer<Value>) {
        mutex.withLock {
            if (value == null) {
                cache.remove(key)
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

                cache.put(key, entry)
            }
        }
    }

    override suspend fun remove(key: String) {
        mutex.withLock {
            cache.remove(key)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            cache.evictAll()
        }
    }
}
