package com.mooncloak.vpn.app.shared.storage.database

public interface DatabaseManager<Database> : AutoCloseable {

    public fun get(): Database

    public override fun close()

    public companion object
}
