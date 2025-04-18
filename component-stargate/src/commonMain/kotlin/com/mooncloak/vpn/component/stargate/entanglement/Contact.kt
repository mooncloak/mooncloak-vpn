package com.mooncloak.vpn.component.stargate.entanglement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents information about the contact. Not to be confused with Profile which is Identity defined, this Contact
 * model is user defined.
 */
@Serializable
public data class Contact public constructor(
    @SerialName(value = "name") public val name: String
)
