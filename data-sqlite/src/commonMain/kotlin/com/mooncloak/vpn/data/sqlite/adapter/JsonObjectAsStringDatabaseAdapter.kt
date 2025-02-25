package com.mooncloak.vpn.data.sqlite.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * A [ColumnAdapter] implementation that stores [JsonElement] values as JSON encoded [String]
 * values in the database.
 *
 * @property [json] The [Json] instance used to encode and decode the [JsonObject]s. Defaults to
 * [Json.Default].
 *
 * @see [DatabaseAdapter.Companion.jsonElementAsString] for creating an instance of this class.
 */
public class JsonObjectDatabaseAdapter public constructor(
    private val json: Json = Json.Default
) : DatabaseAdapter<JsonObject, String> {

    override fun decode(databaseValue: String): JsonObject =
        json.decodeFromString(
            deserializer = JsonObject.serializer(),
            string = databaseValue
        )

    override fun encode(value: JsonObject): String =
        json.encodeToString(
            serializer = JsonObject.serializer(),
            value = value
        )
}
