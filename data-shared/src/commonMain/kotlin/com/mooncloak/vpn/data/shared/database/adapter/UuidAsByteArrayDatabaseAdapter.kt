package com.mooncloak.vpn.data.shared.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * A [ColumnAdapter] implementation that stores [Uuid] values as [ByteArray] values, formatted as
 * [Uuid.toByteArray], in the database.
 *
 * @see [DatabaseAdapter.Companion.uuidAsByteArray] for accessing this object.
 */
@ExperimentalUuidApi
public object UuidAsByteArrayDatabaseAdapter : DatabaseAdapter<Uuid, ByteArray> {

    override fun decode(databaseValue: ByteArray): Uuid =
        Uuid.fromByteArray(databaseValue)

    override fun encode(value: Uuid): ByteArray = value.toByteArray()
}
