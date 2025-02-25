package com.mooncloak.vpn.data.shared.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A [ColumnAdapter] implementation that stores [Uuid] values as hexadecimal [String] values,
 * formatted via [Uuid.toHexString], in the database.
 *
 * @see [DatabaseAdapter.Companion.uuidAsHexString] for accessing this object.
 */
@ExperimentalUuidApi
public object UuidAsHexStringDatabaseAdapter : DatabaseAdapter<Uuid, String> {

    override fun decode(databaseValue: String): Uuid =
        Uuid.parseHex(databaseValue)

    override fun encode(value: Uuid): String = value.toHexString()
}
