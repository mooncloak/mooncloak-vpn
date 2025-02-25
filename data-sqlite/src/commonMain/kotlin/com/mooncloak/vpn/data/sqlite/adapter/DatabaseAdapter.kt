package com.mooncloak.vpn.data.sqlite.adapter

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.adapter.primitive.ShortColumnAdapter
import kotlinx.datetime.Instant
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A [ColumnAdapter] wrapper interface. This wrapper is created to consolidate the creation of the
 * supported adapters.
 *
 * @see [DatabaseAdapter.Companion.instantAsIsoString] for accessing the
 * [InstantAsIsoStringDatabaseAdapter] object.
 *
 * @see [DatabaseAdapter.Companion.instantAsMillisecondsLong] for accessing the
 * [InstantAsMillisecondsLongDatabaseAdapter] object.
 *
 * @see [DatabaseAdapter.Companion.jsonElementAsString] for creating an instance of the
 * [JsonElementDatabaseAdapter] class.
 *
 * @see [DatabaseAdapter.Companion.serializableAsString] for creating an instance of the
 * [SerializableValueAsStringDatabaseAdapter] class.
 *
 * @see [DatabaseAdapter.Companion.serializableAsByteArray] for creating an instance of the
 * [SerializableValueAsByteArrayDatabaseAdapter] class.
 */
public interface DatabaseAdapter<T : Any, S> : ColumnAdapter<T, S> {

    public companion object
}

/**
 * A convenience function for accessing the [InstantAsIsoStringDatabaseAdapter].
 */
public fun DatabaseAdapter.Companion.instantAsIsoString(): DatabaseAdapter<Instant, String> =
    InstantAsIsoStringDatabaseAdapter

/**
 * A convenience function for accessing the [InstantAsMillisecondsLongDatabaseAdapter].
 */
public fun DatabaseAdapter.Companion.instantAsMillisecondsLong(): DatabaseAdapter<Instant, Long> =
    InstantAsMillisecondsLongDatabaseAdapter

/**
 * A convenience function for creating a [JsonElementDatabaseAdapter] instance.
 */
public fun DatabaseAdapter.Companion.jsonElementAsString(
    json: Json = Json.Default
): DatabaseAdapter<JsonElement, String> =
    JsonElementDatabaseAdapter(json = json)

/**
 * A convenience function for creating a [JsonObjectDatabaseAdapter] instance.
 */
public fun DatabaseAdapter.Companion.jsonObjectAsString(
    json: Json = Json.Default
): DatabaseAdapter<JsonObject, String> =
    JsonObjectDatabaseAdapter(json = json)

/**
 * A convenience function for creating a [SerializableValueAsStringDatabaseAdapter] instance.
 */
public inline fun <reified T : Any> DatabaseAdapter.Companion.serializableAsString(
    format: StringFormat = Json.Default,
    serializer: KSerializer<T> = format.serializersModule.serializer()
): DatabaseAdapter<T, String> = SerializableValueAsStringDatabaseAdapter(
    format = format,
    serializer = serializer
)

/**
 * A convenience function for creating a [SerializableValueAsByteArrayDatabaseAdapter] instance.
 */
public inline fun <reified T : Any> DatabaseAdapter.Companion.serializableAsByteArray(
    format: BinaryFormat,
    serializer: KSerializer<T> = format.serializersModule.serializer()
): DatabaseAdapter<T, ByteArray> = SerializableValueAsByteArrayDatabaseAdapter(
    format = format,
    serializer = serializer
)

/**
 * A convenience function for accessing the [IntColumnAdapter].
 */
public fun DatabaseAdapter.Companion.intAsLong(): ColumnAdapter<Int, Long> =
    IntColumnAdapter

/**
 * A convenience function for accessing the [ShortColumnAdapter].
 */
public fun DatabaseAdapter.Companion.shortAsLong(): ColumnAdapter<Short, Long> =
    ShortColumnAdapter

/**
 * A convenience function for accessing the [FloatColumnAdapter].
 */
public fun DatabaseAdapter.Companion.floatAsDouble(): ColumnAdapter<Float, Double> =
    FloatColumnAdapter

/**
 * A convenience function for accessing the [BooleanAsIntDatabaseAdapter].
 */
public fun DatabaseAdapter.Companion.booleanAsInt(): ColumnAdapter<Boolean, Int> =
    BooleanAsIntDatabaseAdapter

/**
 * A convenience function for accessing the [UuidAsStringDatabaseAdapter].
 */
@ExperimentalUuidApi
public fun DatabaseAdapter.Companion.uuidAsString(): DatabaseAdapter<Uuid, String> =
    UuidAsStringDatabaseAdapter

/**
 * A convenience function for accessing the [UuidAsStringDatabaseAdapter].
 */
@ExperimentalUuidApi
public fun DatabaseAdapter.Companion.uuidAsHexString(): DatabaseAdapter<Uuid, String> =
    UuidAsHexStringDatabaseAdapter

/**
 * A convenience function for accessing the [UuidAsStringDatabaseAdapter].
 */
@ExperimentalUuidApi
public fun DatabaseAdapter.Companion.uuidAsByteArray(): DatabaseAdapter<Uuid, ByteArray> =
    UuidAsByteArrayDatabaseAdapter
