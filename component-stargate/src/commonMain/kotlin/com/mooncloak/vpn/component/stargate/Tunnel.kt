package com.mooncloak.vpn.component.stargate

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialFormat
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

public interface Tunnel : AutoCloseable {

    public val isConnected: Boolean

    public val connectionPoints: List<ConnectionPoint>

    public override fun close()

    public companion object
}

public interface RawTunnel : Tunnel {

    @Throws(IllegalStateException::class, IOException::class, CancellationException::class)
    public suspend fun source(): Source

    @Throws(IllegalStateException::class, IOException::class, CancellationException::class)
    public suspend fun sink(): Sink

    @Throws(IllegalStateException::class, IOException::class, CancellationException::class)
    public suspend fun send(block: suspend Sink.() -> Unit) {
        val sink = sink()

        block.invoke(sink)
    }

    @Throws(IllegalStateException::class, IOException::class, CancellationException::class)
    public suspend fun receive(block: suspend Source.() -> Unit) {
        val source = source()

        block.invoke(source)
    }

    public companion object
}

public interface SerialTunnel<T : Any> : Tunnel {

    public val format: SerialFormat

    public val serializer: KSerializer<T>

    public val kClass: KClass<T>

    @Throws(IllegalStateException::class, IOException::class, CancellationException::class)
    public suspend fun send(value: T?)

    @Throws(IllegalStateException::class, IOException::class, CancellationException::class)
    public suspend fun receive(): T?

    public fun receiveAll(): Flow<T?>

    public companion object
}

public abstract class BaseSerialTunnel<T : Any> public constructor(
    private val dispatcher: CoroutineDispatcher
) : SerialTunnel<T> {

    protected abstract val contentWrapper: ContentWrapper

    protected abstract suspend fun source(): Source

    protected abstract suspend fun sink(): Sink

    protected abstract suspend fun Sink.encode(value: T?)

    protected abstract suspend fun Source.decode(): T?

    private val mutex = Mutex(locked = false)

    private val stateFlow = MutableStateFlow<ReceivedContent<T>?>(null)

    final override suspend fun send(value: T?) {
        withContext(dispatcher) {
            mutex.withLock {
                if (!isConnected) {
                    error("Cannot send data. Tunnel is not connected.")
                }

                val sink = sink()

                contentWrapper.wrap(sink) {
                    this.encode(value)
                }
            }
        }
    }

    final override suspend fun receive(): T? =
        withContext(dispatcher) {
            mutex.withLock {
                if (!isConnected){
                    error("Cannot receive data. Tunnel is not connected.")
                }

                var value: T? = null

                val source = source()

                contentWrapper.unwrap(source) {
                    value = this.decode()
                }

                stateFlow.emit(ReceivedContent(value = value))

                return@withContext value
            }
        }

    final override fun receiveAll(): Flow<T?> =
        stateFlow.filterNotNull()
            .map { content -> content.value }

    override fun close() {
    }
}

private data class ReceivedContent<T : Any>(
    val value: T?
)
