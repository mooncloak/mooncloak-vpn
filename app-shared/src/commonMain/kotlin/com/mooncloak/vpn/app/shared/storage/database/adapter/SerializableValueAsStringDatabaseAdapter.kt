package com.mooncloak.vpn.app.shared.storage.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json

/**
 * A [ColumnAdapter] implementation that stores any [Serializable] value as a [String] in the
 * database using the provided [format] to encode and decode the value.
 *
 * @property [format] The [StringFormat] used to encode and decode the serializable value. Defaults
 * to [Json.Default].
 *
 * @property [serializer] The [KSerializer] used to encode and decode the type [T].
 *
 * @see [DatabaseAdapter.Companion.serializableAsByteArray] for creating an instance of this class.
 */
public open class SerializableValueAsStringDatabaseAdapter<T : Any> public constructor(
    private val format: StringFormat = Json.Default,
    private val serializer: KSerializer<T>
) : DatabaseAdapter<T, String> {

    final override fun decode(databaseValue: String): T =
        format.decodeFromString(
            deserializer = serializer,
            string = databaseValue
        )

    final override fun encode(value: T): String =
        format.encodeToString(
            serializer = serializer,
            value = value
        )
}
