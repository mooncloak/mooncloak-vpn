package com.mooncloak.vpn.data.shared.keyvalue

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * A [MutableKeyValueStorage] implementation that uses [Settings] to store and retrieve data.
 *
 * This class provides a way to persist key-value pairs using the underlying [Settings] storage. It supports storing
 * and retrieving values of any type that can be serialized and deserialized by a [KSerializer]. All operations are
 * thread-safe, ensuring data consistency when accessed concurrently.
 *
 * The class utilizes a [Mutex] to protect access to the underlying [Settings] instance, preventing data corruption or
 * inconsistencies due to concurrent reads and writes.
 *
 * @property [settings] The [Settings] instance used for data storage.
 *
 * @property [serializersModule] The [SerializersModule] used for (de)serialization. Defaults to
 * [EmptySerializersModule].
 *
 * @constructor Creates a new [SettingsKeyValueStorage] instance.
 */
@OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
public open class SettingsKeyValueStorage public constructor(
    private val settings: Settings,
    private val serializersModule: SerializersModule = EmptySerializersModule()
) : MutableKeyValueStorage {

    // We need to protect all access to the settings, otherwise it can get in a corrupted state. So both reads and
    // writes are surrounded with locks by the same mutex to prevent data inconsistencies.
    private val mutex = Mutex(locked = false)
    private val emitMutex = Mutex(locked = false)

    private val eventFlow = MutableStateFlow<UpdateEvent?>(null)

    override suspend fun contains(key: String): Boolean {
        require(key.isNotBlank()) { "Key must not be blank." }

        return mutex.withLock {
            settings.contains(key)
        }
    }

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
        require(key.isNotBlank()) { "Key must not be blank." }

        return mutex.withLock {
            settings.decodeValueOrNull(
                key = key,
                serializer = deserializer,
                serializersModule = serializersModule
            )
        }
    }

    override suspend fun <Value : Any> set(key: String, value: Value?, serializer: KSerializer<Value>) {
        require(key.isNotBlank()) { "Key must not be blank." }

        mutex.withLock {
            if (value == null) {
                settings.remove(key = key)
            } else {
                settings.encodeValue(
                    serializer = serializer,
                    key = key,
                    value = value,
                    serializersModule = serializersModule
                )
            }

            emit(
                key = key,
                value = value,
                serializer = serializer
            )
        }
    }

    override suspend fun remove(key: String) {
        require(key.isNotBlank()) { "Key must not be blank." }

        mutex.withLock {
            settings.remove(key)

            emit(
                key = key,
                value = null,
                serializer = null
            )
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            val keys = settings.keys

            settings.clear()

            keys.forEach { key ->
                emit(
                    key = key,
                    value = null,
                    serializer = null
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <Value : Any> flow(key: String, deserializer: KSerializer<Value>): Flow<Value?> {
        require(key.isNotBlank()) { "Key must not be blank." }

        return eventFlow.filter { event ->
            event?.key == key && (event.serializer == deserializer || event.value == null)
        }.map { event ->
            event?.value as? Value
        }
    }

    private suspend inline fun <Value> emit(
        key: String,
        value: Value?,
        serializer: KSerializer<Value>?
    ) {
        emitMutex.withLock {
            eventFlow.value = UpdateEvent(
                key = key,
                value = value,
                serializer = serializer
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SettingsKeyValueStorage) return false

        if (settings != other.settings) return false
        if (serializersModule != other.serializersModule) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = settings.hashCode()
        result = 31 * result + serializersModule.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String =
        "SettingsKeyValueStorage(settings=$settings, serializersModule=$serializersModule, mutex=$mutex)"
}

private data class UpdateEvent(
    val key: String,
    val value: Any?,
    val serializer: KSerializer<*>?
)
