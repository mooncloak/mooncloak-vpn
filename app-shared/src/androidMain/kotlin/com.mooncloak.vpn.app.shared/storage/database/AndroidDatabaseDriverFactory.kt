package com.mooncloak.vpn.app.shared.storage.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase

public class AndroidDatabaseDriverFactory @Inject public constructor(
    private val context: ApplicationContext
) : DatabaseDriverFactory {

    override fun create(): SqlDriver =
        AndroidSqliteDriver(
            MooncloakDatabase.Schema,
            context,
            "mooncloak_vpn.db"
        )
}
