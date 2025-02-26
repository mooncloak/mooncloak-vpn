package com.mooncloak.vpn.data.shared.cache

import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlin.time.Duration

/**
 * A [MutableKeyValueStorage] implementation specifically used as a cache.
 */
public interface Cache : MutableKeyValueStorage {

    public companion object
}

/**
 * Retrieves a new [Cache] instance using the provided values.
 *
 * @param [format] The [StringFormat] used to serializer and deserialize the cache values to and from their stored
 * [String] values. Defaults to [Json.Default].
 *
 * @param [maxSize] The maximum amount of items to store in the cache. If `null` is provided, no maximum will be set.
 * Defaults to `null`.
 *
 * @param [expirationAfterWrite] The [Duration] representing the time period to expire an item after it has been
 * written to the cache. If `null` is provided, no expiration period will be set.
 */
public expect fun Cache.Companion.create(
    format: StringFormat = Json.Default,
    maxSize: Int? = null,
    expirationAfterWrite: Duration? = null
): Cache
