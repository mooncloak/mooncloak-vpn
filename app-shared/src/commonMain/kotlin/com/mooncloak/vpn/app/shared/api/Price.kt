package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the association of a [currency] and an [amount] for the purposes of an amount of a
 * [Currency] that needs to be exchanged in order to obtain an item or commodity.
 *
 * @property [currency] The [Currency] that is used for the exchange of a commodity. Serialized as
 * a non-null required [Currency] object value.
 *
 * @property [amount] The amount of the [currency] that is required for an exchange for a commodity.
 * Serialized as a non-null required value. This is a count of the minor units (ex: cents for USD) of the [currency].
 *
 * @property [formatted] A formatted [String] representation of this [Price] (ex: "$10").
 */
@Immutable
@Serializable
public data class Price public constructor(
    @SerialName(value = "currency") public val currency: Currency,
    @SerialName(value = "amount") public val amount: Long,
    @SerialName(value = "formatted") public val formatted: String? = null
)
