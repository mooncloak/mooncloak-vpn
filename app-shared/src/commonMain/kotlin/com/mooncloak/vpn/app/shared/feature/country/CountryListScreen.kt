package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.FlagImage
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.composable.CountryListItem
import com.mooncloak.vpn.app.shared.feature.country.composable.ErrorCard
import com.mooncloak.vpn.app.shared.feature.country.composable.Label
import com.mooncloak.vpn.app.shared.feature.country.composable.RegionListItem
import com.mooncloak.vpn.app.shared.feature.server.region.RegionServerListScreen
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_back
import com.mooncloak.vpn.app.shared.resource.country_list_default_region_type
import com.mooncloak.vpn.app.shared.resource.country_list_description
import com.mooncloak.vpn.app.shared.resource.country_list_error_description_no_countries
import com.mooncloak.vpn.app.shared.resource.country_list_error_title_no_countries
import com.mooncloak.vpn.app.shared.resource.country_list_header_label_with_count
import com.mooncloak.vpn.app.shared.resource.country_list_title
import com.mooncloak.vpn.app.shared.resource.region_list_description
import com.mooncloak.vpn.app.shared.resource.region_list_title
import com.mooncloak.vpn.app.shared.util.LaunchLazyLoader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CountryListScreen(
    sheetState: ManagedModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createCountryListComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    if (!viewModel.state.current.value.append.endOfPaginationReached) {
        LaunchLazyLoader(
            lazyListState = lazyListState,
            onLoadMore = {
                viewModel.loadMore()
            }
        )
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = if (viewModel.state.current.value.selectedCountry == null) {
                stringResource(Res.string.country_list_title)
            } else {
                viewModel.state.current.value.selectedCountry?.country?.name
                    ?: stringResource(Res.string.region_list_title)
            },
            description = if (viewModel.state.current.value.selectedCountry == null) {
                stringResource(Res.string.country_list_description)
            } else {
                stringResource(Res.string.region_list_description)
            },
            icon = (@Composable {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.select(country = null) }
                            .padding(8.dp),
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(Res.string.cd_action_back),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    FlagImage(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .size(36.dp),
                        imageUri = viewModel.state.current.value.selectedCountry?.country?.flag,
                    )
                }
            }).takeIf { viewModel.state.current.value.selectedCountry != null },
            snackbarHostState = snackbarHostState,
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item(key = "ContentLabel") {
                    Label(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp),
                        value = if (viewModel.state.current.value.selectedCountry == null) {
                            stringResource(
                                Res.string.country_list_header_label_with_count,
                                stringResource(Res.string.country_list_title),
                                viewModel.state.current.value.countries?.size ?: 0
                            )
                        } else {
                            stringResource(
                                Res.string.country_list_header_label_with_count,
                                viewModel.state.current.value.selectedCountry?.country?.regionType
                                    ?: stringResource(Res.string.country_list_default_region_type),
                                viewModel.state.current.value.selectedCountry?.regions?.size ?: 0
                            )
                        }
                    )
                }

                if (viewModel.state.current.value.selectedCountry != null) {
                    items(
                        items = viewModel.state.current.value.selectedCountry?.regions ?: emptyList(),
                        key = { details -> details.region.code.value },
                        contentType = { "RegionListItem" }
                    ) { details ->
                        RegionListItem(
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    // TODO: Connect to region
                                },
                            region = details.region,
                            onMoreSelected = {
                                // TODO: Launch server list for regions
                            }
                        )
                    }
                }

                if (viewModel.state.current.value.selectedCountry == null) {
                    items(
                        items = viewModel.state.current.value.countries,
                        key = { details -> details.country.code.value },
                        contentType = { "CountryListItem" }
                    ) { details ->
                        CountryListItem(
                            modifier = Modifier
                                .sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
                                .clickable {
                                    // TODO: Connect to country
                                },
                            country = details.country,
                            onMoreSelected = {
                                coroutineScope.launch {
                                    viewModel.select(country = details)
                                }
                            }
                        )
                    }
                }

                if (viewModel.state.current.value.countries.isEmpty() && !viewModel.state.current.value.isLoading) {
                    item(key = "EmptyCountryListError") {
                        ErrorCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
                                .padding(12.dp),
                            title = stringResource(Res.string.country_list_error_title_no_countries),
                            description = stringResource(Res.string.country_list_error_description_no_countries)
                        )
                    }
                }
            }
        }
    }

    /* TODO
    RegionServerListScreen(
        modifier = Modifier.fillMaxWidth(),
        country = destination.country,
        region = destination.region,
        onConnect = onConnectToServer
    )*/
}
