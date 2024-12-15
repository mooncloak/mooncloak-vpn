package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.vpn.app.shared.composable.rememberModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.composable.CountryListBottomSheet
import com.mooncloak.vpn.app.shared.feature.country.composable.CountryListItem
import com.mooncloak.vpn.app.shared.feature.country.composable.NoVPNServersCard
import com.mooncloak.vpn.app.shared.feature.country.di.createCountryListComponent
import com.mooncloak.vpn.app.shared.feature.country.model.CountryListBottomSheetDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.destination_main_countries_title
import com.mooncloak.vpn.app.shared.util.LaunchLazyLoader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CountryListScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createCountryListComponent(presentationDependencies = this)
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
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

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = topAppBarBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Text(text = stringResource(Res.string.destination_main_countries_title))
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                item(key = "TopSpacing") {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateTopPadding()))
                }

                items(
                    items = viewModel.state.current.value.countries,
                    key = { country -> country.code.value },
                    contentType = { "CountryListItem" }
                ) { country ->
                    CountryListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {

                            },
                        country = country,
                        onMoreSelected = {
                            coroutineScope.launch {
                                bottomSheetState.show(
                                    destination = CountryListBottomSheetDestination.RegionList(
                                        country = country
                                    )
                                )
                            }
                        }
                    )
                }

                if (viewModel.state.current.value.countries.isEmpty() && !viewModel.state.current.value.isLoading) {
                    item(key = "EmptyCountryListError") {
                        NoVPNServersCard(
                            modifier = Modifier.fillMaxWidth()
                                .padding(12.dp)
                        )
                    }
                }

                item(key = "BottomSpacing") {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateBottomPadding() + 28.dp))
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewModel.state.current.value.refresh is LoadState.Loading
            ) {
                CircularProgressIndicator()
            }
        }
    }

    CountryListBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        state = bottomSheetState,
        onConnectToRegion = { region ->},
        onConnectToServer = { server -> }
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
