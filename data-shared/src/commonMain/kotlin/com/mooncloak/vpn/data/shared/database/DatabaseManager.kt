package com.mooncloak.vpn.data.shared.database

/**
 * Manages a database instance, providing access to it and handling its lifecycle.
 *
 * This interface extends [DatabaseProvider] to offer a way to retrieve the managed database instance and
 * [AutoCloseable] to manage the database's resources, ensuring they are released properly.
 *
 * @param [Database] The type of the database being managed. Must be a non-nullable type.
 */
public interface DatabaseManager<Database : Any> : DatabaseProvider<Database>,
    AutoCloseable {

    public override fun get(): Database

    public override fun close()

    public companion object
}
