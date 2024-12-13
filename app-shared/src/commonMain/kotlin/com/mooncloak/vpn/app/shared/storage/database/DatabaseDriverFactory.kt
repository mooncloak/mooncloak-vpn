package com.mooncloak.vpn.app.shared.storage.database

import app.cash.sqldelight.db.SqlDriver

public fun interface DatabaseDriverFactory {

    public fun create(): SqlDriver

    public companion object
}
