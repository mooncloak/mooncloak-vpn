package com.mooncloak.vpn.data.sqlite

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

public operator fun SqlDriverFactory.Companion.invoke(
    fileName: String,
    schema: SqlSchema<QueryResult.Value<Unit>>
): SqlDriverFactory = NativeSqlDriverFactory(
    fileName = fileName,
    schema = schema
)

internal class NativeSqlDriverFactory internal constructor(
    private val fileName: String,
    private val schema: SqlSchema<QueryResult.Value<Unit>>
) : SqlDriverFactory {

    override fun create(): SqlDriver = NativeSqliteDriver(schema, fileName)
}
