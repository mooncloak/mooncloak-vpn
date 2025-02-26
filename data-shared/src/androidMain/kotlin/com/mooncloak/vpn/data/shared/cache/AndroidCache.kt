package com.mooncloak.vpn.data.shared.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

public actual fun com.mooncloak.vpn.data.shared.cache.Cache.Companion.create(
    format: StringFormat,
    maxSize: Int?,
    expirationAfterWrite: Duration?
): com.mooncloak.vpn.data.shared.cache.Cache {
    var cacheBuilder = Caffeine.newBuilder()

    if (maxSize != null) {
        cacheBuilder = cacheBuilder.maximumSize(maxSize.toLong())
    }

    if (expirationAfterWrite != null) {
        cacheBuilder = cacheBuilder.expireAfterWrite(expirationAfterWrite.inWholeMilliseconds, TimeUnit.MILLISECONDS)
    }

    return AndroidCache(
        format = format,
        cache = cacheBuilder.build()
    )
}

internal class AndroidCache internal constructor(
    private val format: StringFormat,
    private val cache: Cache<String, String>
) : com.mooncloak.vpn.data.shared.cache.Cache {

    private val mutex = Mutex(locked = false)

    override suspend fun contains(key: String): Boolean =
        cache.getIfPresent(key) != null

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
        val stored = cache.getIfPresent(key) ?: return null

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
                cache.invalidate(key)
            } else {
                val stored = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                cache.put(key, stored)
            }
        }
    }

    override suspend fun remove(key: String) {
        mutex.withLock {
            cache.invalidate(key)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            cache.invalidateAll()
        }
    }
}
