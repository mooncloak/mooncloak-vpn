package com.mooncloak.vpn.component.stargate

import kotlinx.io.Sink
import kotlinx.io.Source

public interface ContentWrapper {

    public suspend fun wrap(sink: Sink, block: suspend Sink.() -> Unit)

    public suspend fun unwrap(source: Source, block: suspend Source.() -> Unit)

    public companion object
}
