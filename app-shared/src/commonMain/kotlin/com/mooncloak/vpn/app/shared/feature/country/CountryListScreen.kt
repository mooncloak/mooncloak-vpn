package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.rememberModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.composable.CountryListBottomSheet
import com.mooncloak.vpn.app.shared.feature.country.composable.CountryListItem
import com.mooncloak.vpn.app.shared.feature.country.composable.ErrorCard
import com.mooncloak.vpn.app.shared.feature.country.model.CountryListBottomSheetDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_error_description_no_countries
import com.mooncloak.vpn.app.shared.resource.country_list_error_title_no_countries
import com.mooncloak.vpn.app.shared.resource.country_list_title
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
    val bottomSheetState = rememberModalNavigationBottomSheetState<CountryListBottomSheetDestination>()
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
            title = stringResource(Res.string.country_list_title),
            snackbarHostState = snackbarHostState,
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                            },
                        country = details.country,
                        onMoreSelected = {
                            coroutineScope.launch {
                                bottomSheetState.show(
                                    destination = CountryListBottomSheetDestination.RegionList(
                                        country = details.country
                                    )
                                )
                            }
                        }
                    )
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

    CountryListBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        state = bottomSheetState,
        onConnectToRegion = { region -> },
        onConnectToServer = { server -> }
    )
}
