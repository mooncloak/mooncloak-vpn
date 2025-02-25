package com.mooncloak.vpn.data.sqlite.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

/**
 * A [ColumnAdapter] implementation that stores [Instant] values as ISO-8601 [String] values in the
 * database.
 *
 * @see [DatabaseAdapter.Companion.instantAsIsoString] for accessing this object.
 */
public object InstantAsIsoStringDatabaseAdapter : DatabaseAdapter<Instant, String> {

    override fun decode(databaseValue: String): Instant =
        Instant.parse(databaseValue)

    override fun encode(value: Instant): String =
        value.toString()
}
