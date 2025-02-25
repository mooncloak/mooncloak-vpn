package com.mooncloak.vpn.data.shared.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * A [ColumnAdapter] implementation that stores any [Serializable] value as a [ByteArray] in the
 * database using the provided [format] to encode and decode the value.
 *
 * @property [format] The [BinaryFormat] used to encode and decode the serializable value.
 *
 * @property [serializer] The [KSerializer] used to encode and decode the type [T].
 *
 * @see [DatabaseAdapter.Companion.serializableAsByteArray] for creating an instance of this class.
 */
public open class SerializableValueAsByteArrayDatabaseAdapter<T : Any> public constructor(
    private val format: BinaryFormat,
    private val serializer: KSerializer<T>
) : DatabaseAdapter<T, ByteArray> {

    final override fun decode(databaseValue: ByteArray): T =
        format.decodeFromByteArray(
            deserializer = serializer,
            bytes = databaseValue
        )

    final override fun encode(value: T): ByteArray =
        format.encodeToByteArray(
            serializer = serializer,
            value = value
        )
}
