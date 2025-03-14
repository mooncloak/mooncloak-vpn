package com.mooncloak.vpn.app.shared.feature.country.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.location.RegionDetails
import com.mooncloak.vpn.network.core.vpn.isToggling
import com.mooncloak.vpn.app.shared.feature.country.composable.ErrorCard
import com.mooncloak.vpn.app.shared.feature.country.composable.Label
import com.mooncloak.vpn.app.shared.feature.country.composable.RegionListItem
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.network.core.vpn.VPNConnection
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RegionListLayout(
    connection: VPNConnection,
    regions: List<RegionDetails>,
    label: String,
    loading: Boolean,
    lazyListState: LazyListState,
    errorTitle: String?,
    errorDescription: String? = null,
    onConnect: (details: RegionDetails) -> Unit,
    onDetails: (details: RegionDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "ContentLabel") {
            Label(
                modifier = Modifier.sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = label
            )
        }

        items(
            items = regions,
            key = { details -> details.region.code.value },
            contentType = { "RegionListItem" }
        ) { details ->
            RegionListItem(
                modifier = Modifier
                    .sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .clickable(enabled = !connection.isToggling()) {
                        onConnect.invoke(details)
                    },
                region = details.region,
                onMoreSelected = {
                    onDetails.invoke(details)
                }
            )
        }

        if ((regions.isEmpty() && !loading) || (!errorTitle.isNullOrBlank())) {
            item(key = "ErrorCard") {
                ErrorCard(
                    modifier = Modifier.sizeIn(maxWidth = 600.dp)
                        .fillMaxWidth()
                        .padding(12.dp),
                    title = errorTitle ?: stringResource(Res.string.global_unexpected_error),
                    description = errorDescription
                )
            }
        }
    }
}
