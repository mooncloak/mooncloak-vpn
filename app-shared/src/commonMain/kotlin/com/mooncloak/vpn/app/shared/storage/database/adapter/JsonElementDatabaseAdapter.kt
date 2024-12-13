package com.mooncloak.vpn.app.shared.storage.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * A [ColumnAdapter] implementation that stores [JsonElement] values as JSON encoded [String]
 * values in the database.
 *
 * @property [json] The [Json] instance used to encode and decode the [JsonElement]s. Defaults to
 * [Json.Default].
 *
 * @see [DatabaseAdapter.Companion.jsonElementAsString] for creating an instance of this class.
 */
public class JsonElementDatabaseAdapter public constructor(
    private val json: Json = Json.Default
) : DatabaseAdapter<JsonElement, String> {

    override fun decode(databaseValue: String): JsonElement =
        json.decodeFromString(
            deserializer = JsonElement.serializer(),
            string = databaseValue
        )

    override fun encode(value: JsonElement): String =
        json.encodeToString(
            serializer = JsonElement.serializer(),
            value = value
        )
}
