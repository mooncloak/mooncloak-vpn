package com.mooncloak.vpn.component.stargate.entanglement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class IdentityHandleFormat(
    public val serialName: String,
    public val separator: String?
) {

    // Ex: Mastodon - @chris@example.com
    @SerialName(value = "activity_pub")
    ACTIVITY_PUB(
        serialName = "activity_pub",
        separator = "@"
    ),

    // Ex: BlueSky - @chris.example.com
    @SerialName(value = "at")
    AT(
        serialName = "at",
        separator = "."
    ),

    // Ex: Matrix - @chris:example.com
    @SerialName(value = "matrix")
    MATRIX(
        serialName = "matrix",
        separator = ":"
    ),

    // Ex: Direct DID - did:web:chris
    @SerialName(value = "did")
    DID(
        serialName = "did",
        separator = null
    ),

    // Ex: Reverse DID - @chris:web:did or @chris:web
    @SerialName(value = "alias")
    ALIAS(
        serialName = "alias",
        separator = null
    ),

    // Ex: Local Part - @chris
    @SerialName(value = "local")
    LOCAL(
        serialName = "local",
        separator = null
    );

    public companion object {

        public operator fun get(serialName: String): IdentityHandleFormat? =
            IdentityHandleFormat.entries.firstOrNull { it.serialName.equals(serialName, ignoreCase = true) }
    }
}
