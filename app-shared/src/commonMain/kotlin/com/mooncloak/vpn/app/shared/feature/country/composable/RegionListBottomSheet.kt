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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.kodetools.locale.Region
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.feature.country.isLoading
import com.mooncloak.vpn.app.shared.feature.country.state.RegionListBottomSheetState
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_default_region_type
import com.mooncloak.vpn.app.shared.resource.country_list_header_label_with_count
import com.mooncloak.vpn.app.shared.resource.country_list_title
import com.mooncloak.vpn.app.shared.resource.region_list_description
import com.mooncloak.vpn.app.shared.resource.region_list_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RegionListBottomSheet(
    state: RegionListBottomSheetState,
    onDetails: (region: Region) -> Unit,
    onConnect: (region: Region) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = state.bottomSheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = state.details.value?.country?.name ?: stringResource(Res.string.region_list_title),
            description = stringResource(Res.string.region_list_description)
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
                            state.details.value?.country?.regionType
                                ?: stringResource(Res.string.country_list_default_region_type),
                            state.details.value?.regions?.size ?: 0
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = SecondaryAlpha
                            )
                        )
                    )
                }

                items(
                    items = state.details.value?.regions ?: emptyList(), // FIXME:  country.regions,
                    key = { details -> details.region.code.value },
                    contentType = { "RegionListItem" }
                ) { details ->
                    RegionListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable { onConnect.invoke(details.region) },
                        region = details.region,
                        onMoreSelected = {
                            onDetails.invoke(details.region)
                        }
                    )
                }
            }
        }
    }
}
