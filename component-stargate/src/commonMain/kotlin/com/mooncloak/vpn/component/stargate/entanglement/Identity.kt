package com.mooncloak.vpn.component.stargate.entanglement

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Serializable
public data class Identity public constructor(
    @SerialName(value = "did") public val did: DID,
    @SerialName(value = "document") public val document: DIDDocument,
    @SerialName(value = "handle") public val handle: IdentityHandle? = null,
    @SerialName(value = "profile") public val profile: Profile? = null,
    @SerialName(value = "contact") public val contact: Contact? = null,
    @SerialName(value = "resolved") public val resolved: Instant
)

public fun Identity.test() {
    if (handle != null) {
        document.alsoKnownAs.filter { it.startsWith("at://") }
    }
}

public fun Identity.isValid(
    at: Instant = Clock.System.now(),
    period: Duration = 15.minutes
): Boolean = (resolved + period) > at
