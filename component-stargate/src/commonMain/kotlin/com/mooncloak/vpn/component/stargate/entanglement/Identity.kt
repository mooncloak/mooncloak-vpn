package com.mooncloak.vpn.component.stargate.entanglement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Identity public constructor(
    @SerialName(value = "did") public val did: DID,
    @SerialName(value = "document") public val document: DIDDocument,
    @SerialName(value = "handle") public val handle: IdentityHandle? = null,
    @SerialName(value = "profile") public val profile: Profile? = null,
    @SerialName(value = "contact") public val contact: Contact? = null
)
