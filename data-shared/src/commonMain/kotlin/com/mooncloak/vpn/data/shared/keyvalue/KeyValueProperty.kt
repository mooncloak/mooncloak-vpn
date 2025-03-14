package com.mooncloak.vpn.data.shared.keyvalue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Represents a property that holds a value associated with a key.
 *
 * This interface provides a way to retrieve the stored value asynchronously.
 *
 * @param Value The type of the value held by this property.
 */
public interface KeyValueProperty<Value : Any> {

    /**
     * Retrieves a value.
     *
     * This function attempts to retrieve a value.
     *
     * @return The retrieved value if it exists, or `null` if no value is available.
     */
    public suspend fun get(): Value?

    /**
     * Retrieves a [Flow] of the underlying value changes.
     */
    public fun flow(): Flow<Value?>

    public companion object
}

/**
 * Represents a key-value property that can be modified.
 *
 * This interface extends [KeyValueProperty] and provides methods to set and remove the associated value.
 *
 * @param Value The type of the value associated with the property. Must be non-nullable.
 */
public interface MutableKeyValueProperty<Value : Any> : KeyValueProperty<Value> {

    /**
     * Sets the current value.
     *
     * This function allows updating the stored value asynchronously.
     *
     * @param value The new value to set. Can be null.
     */
    public suspend fun set(value: Value?)

    /**
     * Removes the current resource or object associated with this instance.
     */
    public suspend fun remove()

    public companion object
}

/**
 * A convenience function for updating the current value if it is already present.
 */
public suspend fun <Value : Any> MutableKeyValueProperty<Value>.update(block: (current: Value) -> Value) {
    val current = this.get()

    if (current != null) {
        val updated = block.invoke(current)

        this.set(value = updated)
    }
}

/**
 * Retrieves a [State] of the underlying [KeyValueProperty] value changes as [KeyValuePropertyLoadingEvent]s.
 */
@Composable
public fun <Value : Any> KeyValueProperty<Value>.loadingEventState(
    context: CoroutineContext = EmptyCoroutineContext
): State<KeyValuePropertyLoadingEvent<Value>> {
    val event = remember {
        mutableStateOf<KeyValuePropertyLoadingEvent<Value>>(KeyValuePropertyLoadingEvent.Loading)
    }

    LaunchedEffect(this) {
        event.value = KeyValuePropertyLoadingEvent.Loaded(value = this@loadingEventState.get())
    }

    return this.flow()
        .map { value -> KeyValuePropertyLoadingEvent.Loaded(value = value) }
        .collectAsState(
            initial = event.value,
            context = context
        )
}

/**
 * Retrieves a [State] of the underlying [KeyValueProperty] value changes starting with [initial].
 *
 * @param [initial] The initial [Value] to set for the state.
 */
@Composable
public fun <Value : Any> KeyValueProperty<Value>.state(
    initial: Value,
    context: CoroutineContext = EmptyCoroutineContext
): State<Value?> {
    val current = remember { mutableStateOf<Value?>(initial) }

    LaunchedEffect(Unit) {
        current.value = this@state.get()
    }

    return this.flow().collectAsState(
        initial = current.value,
        context = context
    )
}
