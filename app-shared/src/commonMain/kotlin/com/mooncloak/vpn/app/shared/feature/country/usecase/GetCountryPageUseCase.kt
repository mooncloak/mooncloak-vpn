package com.mooncloak.vpn.app.shared.feature.country.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.location.CountryPage
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings

public class GetCountryPageUseCase @Inject public constructor(
    private val api: VpnServiceApi,
    private val subscriptionSettings: SubscriptionSettings,
) {

    @OptIn(ExperimentalPaginationAPI::class)
    public suspend operator fun invoke(
        cursor: Cursor? = null,
        direction: Direction = Direction.After,
        count: Int = 20
    ): CountryPage = api.paginateCountries(
        token = subscriptionSettings.tokens.get()?.accessToken,
        direction = direction,
        cursor = cursor,
        count = count.toUInt()
    )
}
