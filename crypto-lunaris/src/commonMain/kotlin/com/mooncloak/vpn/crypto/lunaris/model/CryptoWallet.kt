package com.mooncloak.vpn.crypto.lunaris.model

import com.mooncloak.vpn.api.shared.currency.Currency
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a cryptocurrency wallet with detailed information.
 *
 * @property [id] The unique identifier of this wallet.
 *
 * @property [address] The public address of the cryptocurrency wallet (e.g., "0x...").
 *
 * @property [currency] The cryptocurrency supported by the wallet.
 *
 * @property [created] The [Instant] that the wallet was created.
 *
 * @property [updated] The [Instant] that this wallet was last updated.
 *
 * @property [location] The local file location of the wallet.
 *
 * @property [name] The name of the wallet.
 *
 * @property [note] A user defined note about the wallet.
 */
@Serializable
public data class CryptoWallet public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "updated") public val updated: Instant,
    @SerialName(value = "address") public val address: String,
    @SerialName(value = "currency") public val currency: Currency,
    @SerialName(value = "location") public val location: String,
    @SerialName(value = "name") public val name: String? = null,
    @SerialName(value = "note") public val note: String? = null
)
