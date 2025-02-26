package com.mooncloak.vpn.data.shared.keyvalue

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Creates a read-only property delegate backed by this [MutableKeyValueStorage].
 *
 * This function simplifies the creation of [KeyValueProperty] values that are automatically persisted to the
 * underlying key-value storage. It uses Kotlin's property delegation mechanism for convenient access and modification.
 */
public inline fun <reified Value : Any> MutableKeyValueStorage.property(
    key: String? = null,
    serializer: KSerializer<Value> = serializer()
): ReadOnlyProperty<Any, MutableKeyValueProperty<Value>> =
    KeyValuePropertyDelegate(
        key = key,
        serializer = serializer,
        storage = this
    )

@PublishedApi
internal class KeyValuePropertyDelegate<Value : Any> @PublishedApi internal constructor(
    private val key: String?,
    private val serializer: KSerializer<Value>,
    private val storage: MutableKeyValueStorage
) : ReadOnlyProperty<Any, MutableKeyValueProperty<Value>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableKeyValueProperty<Value> =
        DefaultKeyValueProperty(
            key = key ?: property.name,
            serializer = serializer,
            storage = storage
        )
}
