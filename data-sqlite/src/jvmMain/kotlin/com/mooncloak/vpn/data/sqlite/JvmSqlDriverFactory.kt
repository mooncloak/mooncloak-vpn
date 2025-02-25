package com.mooncloak.vpn.data.sqlite

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info

public operator fun SqlDriverFactory.Companion.invoke(
    filePath: String? = null,
    schema: SqlSchema<QueryResult.Value<Unit>>
): SqlDriverFactory = JvmSqlDriverFactory(
    filePath = filePath,
    schema = schema
)

internal class JvmSqlDriverFactory internal constructor(
    private val filePath: String?,
    private val schema: SqlSchema<QueryResult.Value<Unit>>
) : SqlDriverFactory {

    override fun create(): SqlDriver {
        val url = when {
            filePath.isNullOrBlank() -> JdbcSqliteDriver.IN_MEMORY
            filePath.startsWith("jdbc:sqlite:") -> filePath
            else -> "jdbc:sqlite:$filePath"
        }
        val driver: SqlDriver = JdbcSqliteDriver(url)

        try {
            LogPile.info(
                message = "Attempting to create database tables from schema."
            )

            schema.create(driver)
        } catch (e: Exception) {
            LogPile.error(
                message = "Error creating database tables from schema.",
                cause = e
            )
        }

        return driver
    }
}
