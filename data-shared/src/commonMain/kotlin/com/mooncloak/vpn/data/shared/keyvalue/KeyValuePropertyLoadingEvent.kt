package com.mooncloak.vpn.data.shared.keyvalue

/**
 * Represents the loading event state model for retrieving the value of a [KeyValueProperty].
 */
public sealed class KeyValuePropertyLoadingEvent<out Value : Any> {

    /**
     * Represents the loading state indicating that the value has yet to be loaded so we don't have the current value
     * yet.
     */
    public data object Loading : KeyValuePropertyLoadingEvent<Nothing>()

    /**
     * Represents the loaded state indicating that the current [value] was loaded.
     */
    public data class Loaded<Value : Any>(
        public val value: Value?
    ) : KeyValuePropertyLoadingEvent<Value>()

    public companion object
}
