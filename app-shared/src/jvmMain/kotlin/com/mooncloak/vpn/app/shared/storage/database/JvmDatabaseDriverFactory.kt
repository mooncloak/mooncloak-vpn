package com.mooncloak.vpn.app.shared.storage.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase

public class JvmDatabaseDriverFactory @Inject public constructor() : DatabaseDriverFactory {

    override fun create(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        MooncloakDatabase.Schema.create(driver)
        return driver
    }
}
