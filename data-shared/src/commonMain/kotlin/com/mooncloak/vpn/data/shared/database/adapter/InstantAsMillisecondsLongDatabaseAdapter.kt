package com.mooncloak.vpn.data.shared.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

/**
 * A [ColumnAdapter] implementation that stores [Instant] values as millisecond [Long] values,
 * since the epoch instant 1970-01-01T00:00:00Z, in the database.
 *
 * @see [DatabaseAdapter.Companion.instantAsMillisecondsLong] for accessing this object.
 */
public object InstantAsMillisecondsLongDatabaseAdapter : DatabaseAdapter<Instant, Long> {

    override fun decode(databaseValue: Long): Instant =
        Instant.fromEpochMilliseconds(databaseValue)

    override fun encode(value: Instant): Long =
        value.toEpochMilliseconds()
}
