package com.mooncloak.vpn.data.sqlite.adapter

import app.cash.sqldelight.ColumnAdapter

/**
 * A [ColumnAdapter] implementation that stores [Boolean] values as [Int] values in the database. A
 * stored value of zero means `false`, and a stored value of one means `true`.
 *
 * @see [DatabaseAdapter.Companion.booleanAsInt] for accessing this object.
 */
public object BooleanAsIntDatabaseAdapter : DatabaseAdapter<Boolean, Int> {

    override fun decode(databaseValue: Int): Boolean = databaseValue == 1

    override fun encode(value: Boolean): Int = if (value) 1 else 0
}
