package com.mooncloak.vpn.data.shared.database

import app.cash.sqldelight.db.SqlDriver

/**
 * A factory for creating [SqlDriver] instances.
 *
 * This interface provides a way to create different [SqlDriver] implementations for interacting with SQL databases.
 * Implementations handle the specific details of driver creation, such as database connection setup.
 */
public fun interface SqlDriverFactory {

    /**
     * Creates and returns a new [SqlDriver] instance.
     *
     * This function is responsible for initializing and configuring a new database driver that can be used to interact
     * with a SQLite database.
     *
     * @return A newly created [SqlDriver] instance ready for database operations.
     */
    public fun create(): SqlDriver

    public companion object
}
