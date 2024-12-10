package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.api.Region
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_header_label_with_count
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RegionListLayout(
    regions: List<Region>,
    regionType: String, // Ex: States, Cities, etc.
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item(key = "RegionHeaderLabel") {
            CountryListHeaderLabel(
                text = stringResource(Res.string.country_list_header_label_with_count, regionType, regions.size)
            )
        }

        items(
            items = regions,
            key = { region -> region.code },
            contentType = { "RegionListItem" }
        ) { region ->
            RegionListItem(
                modifier = Modifier.fillMaxWidth(),
                region = region,
                onMoreSelected = {

                }
            )
        }
    }
}
