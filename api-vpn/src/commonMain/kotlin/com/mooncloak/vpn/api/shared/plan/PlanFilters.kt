package com.mooncloak.vpn.api.shared.plan

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PlanFilters public constructor(
    @SerialName(value = Key.PROVIDER) public val provider: BillingProvider? = null,
    @SerialName(value = Key.CURRENCY) public val currency: Currency.Code? = null
) {

    public object Key {

        public const val PROVIDER: String = "provider"
        public const val CURRENCY: String = "currency"
    }
}
