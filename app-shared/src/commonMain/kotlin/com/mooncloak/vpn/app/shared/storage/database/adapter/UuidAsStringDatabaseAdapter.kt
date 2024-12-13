package com.mooncloak.vpn.app.shared.storage.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A [ColumnAdapter] implementation that stores [Uuid] values as [String] values, formatted via
 * [Uuid.toString], in the database.
 *
 * @see [DatabaseAdapter.Companion.uuidAsString] for accessing this object.
 */
@ExperimentalUuidApi
public object UuidAsStringDatabaseAdapter : DatabaseAdapter<Uuid, String> {

    override fun decode(databaseValue: String): Uuid =
        Uuid.parse(databaseValue)

    override fun encode(value: Uuid): String = value.toString()
}
