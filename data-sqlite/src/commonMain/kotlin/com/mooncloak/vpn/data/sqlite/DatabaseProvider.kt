package com.mooncloak.vpn.data.sqlite

/**
 * Interface for providing a database instance.
 *
 * This interface defines a contract for classes that are responsible for providing an instance of a specific database
 * type. The database type is defined by the generic parameter [Database].
 *
 * The interface includes a method [get] to retrieve the database instance and a companion object for potential static
 * utility functions or constants.
 *
 * @param [Database] The type of the database that this provider will supply. Must be a non-nullable type.
 */
public fun interface DatabaseProvider<Database : Any> {

    /**
     * Returns the underlying database instance.
     *
     * @return The Database instance.
     */
    public fun get(): Database

    public companion object
}
