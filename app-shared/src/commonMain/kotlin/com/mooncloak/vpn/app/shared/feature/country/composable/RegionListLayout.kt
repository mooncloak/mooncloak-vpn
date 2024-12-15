package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_default_region_type
import com.mooncloak.vpn.app.shared.resource.country_list_header_label_with_count
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RegionListLayout(
    country: Country,
    onRegionDetails: (region: Region) -> Unit,
    onConnect: (region: Region) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item(key = "RegionHeaderLabel") {
            Text(
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                text = stringResource(
                    Res.string.country_list_header_label_with_count,
                    country.regionType ?: stringResource(Res.string.country_list_default_region_type),
                    country.regions.size
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha))
            )
        }

        items(
            items = country.regions,
            key = { region -> region.code.value },
            contentType = { "RegionListItem" }
        ) { region ->
            RegionListItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable { onConnect.invoke(region) },
                region = region,
                onMoreSelected = {
                    onRegionDetails.invoke(region)
                }
            )
        }
    }
}
