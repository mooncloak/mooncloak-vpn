package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed interface ConnectionStatus {

    public val localCountry: Country?
    public val localIPAddress: String?

    public val connectedServers: List<Server>

    @Immutable
    @Serializable
    @SerialName(value = "disconnected")
    public class Disconnected public constructor(
        @SerialName(value = "country") override val localCountry: Country? = null,
        @SerialName(value = "local_ip") override val localIPAddress: String? = null
    ) : ConnectionStatus {

        override val connectedServers: List<Server> = emptyList()
    }

    @Immutable
    @Serializable
    @SerialName(value = "connecting")
    public class Connecting public constructor(
        @SerialName(value = "country") override val localCountry: Country? = null,
        @SerialName(value = "ip") override val localIPAddress: String? = null,
        @SerialName(value = "servers") override val connectedServers: List<Server> = emptyList()
    ) : ConnectionStatus

    @Immutable
    @Serializable
    @SerialName(value = "connected")
    public class Connected public constructor(
        @SerialName(value = "country") override val localCountry: Country? = null,
        @SerialName(value = "ip") override val localIPAddress: String? = null,
        @SerialName(value = "servers") override val connectedServers: List<Server> = emptyList()
    ) : ConnectionStatus
}
