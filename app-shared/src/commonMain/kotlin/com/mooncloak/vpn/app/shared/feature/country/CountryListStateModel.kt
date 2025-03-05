package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.isActive
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.api.shared.vpn.isToggling
import com.mooncloak.vpn.app.shared.composable.FlagImage
import com.mooncloak.vpn.app.shared.feature.country.model.CountryListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.LayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.RegionListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.ServerListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.canAppendMore
import com.mooncloak.vpn.app.shared.feature.country.model.isLoading
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_description
import com.mooncloak.vpn.app.shared.resource.country_list_title
import com.mooncloak.vpn.app.shared.resource.region_list_description
import com.mooncloak.vpn.app.shared.resource.region_list_title
import com.mooncloak.vpn.app.shared.resource.server_list_description
import com.mooncloak.vpn.app.shared.resource.server_list_header_label
import org.jetbrains.compose.resources.stringResource

@Immutable
public data class CountryListStateModel public constructor(
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val subscription: ServiceSubscription? = null,
    public val layout: LayoutStateModel = CountryListLayoutStateModel(),
    public val errorMessage: String? = null,
    public val hideSheet: Boolean = false
)

public val CountryListStateModel.canAppendMore: Boolean
    inline get() = this.layout.canAppendMore

public val CountryListStateModel.isLoading: Boolean
    inline get() = this.layout.isLoading || this.connection.isToggling()

public val CountryListStateModel.isMember: Boolean
    inline get() = this.subscription != null && this.subscription.isActive()

public val CountryListStateModel.title: String
    @Composable
    inline get() = when (layout) {
        is CountryListLayoutStateModel -> stringResource(Res.string.country_list_title)
        is RegionListLayoutStateModel -> layout.countryDetails.country.name
            ?: stringResource(Res.string.region_list_title)

        is ServerListLayoutStateModel -> stringResource(Res.string.server_list_header_label)
    }

public val CountryListStateModel.description: String
    @Composable
    inline get() = when (layout) {
        is CountryListLayoutStateModel -> stringResource(Res.string.country_list_description)
        is RegionListLayoutStateModel -> stringResource(Res.string.region_list_description)
        is ServerListLayoutStateModel -> stringResource(Res.string.server_list_description)
    }

public val CountryListStateModel.isBackSupported: Boolean
    inline get() = when (layout) {
        is CountryListLayoutStateModel -> false
        is RegionListLayoutStateModel -> true
        is ServerListLayoutStateModel -> true
    }

@Composable
public fun CountryListStateModel.icon() {
    when (layout) {
        is RegionListLayoutStateModel -> FlagImage(
            modifier = Modifier.padding(end = 16.dp)
                .size(36.dp),
            imageUri = layout.countryDetails.country.flag,
        )

        is ServerListLayoutStateModel -> FlagImage(
            modifier = Modifier.padding(end = 16.dp)
                .size(36.dp),
            imageUri = layout.countryDetails.country.flag,
        )

        else -> {}
    }
}
