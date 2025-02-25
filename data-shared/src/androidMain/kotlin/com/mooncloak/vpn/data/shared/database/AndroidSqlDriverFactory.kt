package com.mooncloak.vpn.data.shared.database

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

public operator fun SqlDriverFactory.Companion.invoke(
    context: Context,
    schema: SqlSchema<QueryResult.Value<Unit>>
): SqlDriverFactory = AndroidSqlDriverFactory(context = context, schema = schema)

internal class AndroidSqlDriverFactory internal constructor(
    private val context: Context,
    private val schema: SqlSchema<QueryResult.Value<Unit>>
) : SqlDriverFactory {

    override fun create(): SqlDriver =
        AndroidSqliteDriver(
            schema,
            context,
            "mooncloak_vpn.db"
        )
}
