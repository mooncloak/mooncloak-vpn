package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Represents how long a subscription period lasts.
 *
 * @property [interval] The duration of a single subscription period.
 *
 * @property [amount] The amount of subscription periods included in this trial.
 */
@Immutable
@Serializable
public data class Subscription public constructor(
    @SerialName(value = "interval") public val interval: Duration,
    @SerialName(value = "amount") public val amount: Int
)
