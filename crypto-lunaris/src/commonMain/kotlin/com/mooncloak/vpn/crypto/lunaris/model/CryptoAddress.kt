package com.mooncloak.vpn.crypto.lunaris.model

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CryptoAddress public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "updated") public val updated: Instant,
    @SerialName(value = "address") public val address: String,
    @SerialName(value = "handle") public val handle: String? = null,
    @SerialName(value = "ens_name") public val ensName: String? = null,
    @SerialName(value = "name") public val displayName: String? = null,
    @SerialName(value = "note") public val note: String? = null,
    @SerialName(value = "contact_id") public val contactId: String? = null,
    @SerialName(value = "currency") public val currency: Currency
)
