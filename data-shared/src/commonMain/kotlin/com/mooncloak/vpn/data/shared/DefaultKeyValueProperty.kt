package com.mooncloak.vpn.data.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

internal open class DefaultKeyValueProperty<Value : Any> internal constructor(
    protected val key: String,
    protected val serializer: KSerializer<Value>,
    protected val storage: MutableKeyValueStorage
) : KeyValueProperty<Value>,
    MutableKeyValueProperty<Value> {

    override suspend fun get(): Value? =
        storage.get(
            key = key,
            deserializer = serializer
        )

    override suspend fun set(value: Value?) {
        storage.set(
            key = key,
            value = value,
            serializer = serializer
        )
    }

    override suspend fun remove() {
        storage.remove(key = key)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefaultKeyValueProperty<*>) return false

        if (key != other.key) return false
        if (serializer != other.serializer) return false

        return storage == other.storage
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + serializer.hashCode()
        result = 31 * result + storage.hashCode()
        return result
    }

    override fun toString(): String = "DefaultKeyValueProperty(key='$key', serializer=$serializer, storage=$storage)"
}

internal class DefaultFlowableKeyValueProperty<Value : Any> internal constructor(
    key: String,
    serializer: KSerializer<Value>,
    storage: MutableKeyValueStorage,
    private val flowableStorage: FlowableKeyValueStorage
) : DefaultKeyValueProperty<Value>(
    key = key,
    serializer = serializer,
    storage = storage
), KeyValueProperty<Value>,
    MutableKeyValueProperty<Value>,
    FlowableMutableKeyValueProperty<Value> {

    override fun flow(): Flow<Value?> =
        flowableStorage.flow(
            key = key,
            deserializer = serializer
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DefaultFlowableKeyValueProperty<*>) return false
        if (!super.equals(other)) return false

        return flowableStorage == other.flowableStorage
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + flowableStorage.hashCode()
        return result
    }

    override fun toString(): String = "DefaultFlowableKeyValueProperty(flowableStorage=$flowableStorage)"
}
