package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Represents how long a trial period lasts.
 *
 * @property [interval] The duration of a single trial period.
 *
 * @property [amount] The amount of trial periods included in this trial.
 */
@Immutable
@Serializable
public data class TrialPeriod public constructor(
    @SerialName(value = "interval") public val interval: Duration,
    @SerialName(value = "amount") public val amount: Int
)
