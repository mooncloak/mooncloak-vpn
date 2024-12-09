package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.Country
import com.mooncloak.vpn.app.shared.api.CountryCode
import com.mooncloak.vpn.app.shared.api.Region
import com.mooncloak.vpn.app.shared.api.RegionCode
import com.mooncloak.vpn.app.shared.feature.country.composable.CountryListItem
import com.mooncloak.vpn.app.shared.feature.country.composable.RegionListItem
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.destination_main_countries_title
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CountryListScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { CountryListViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = topAppBarBehavior,
                title = {
                    Text(text = stringResource(Res.string.destination_main_countries_title))
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 12.dp),
            state = lazyListState
        ) {
            item {
                CountryListItem(
                    country = Country(
                        code = CountryCode(value = "us"),
                        regions = emptyList(),
                        name = "United States",
                        flag = null
                    ),
                    onMoreSelected = {

                    }
                )
            }

            item {
                RegionListItem(
                    region = Region(
                        code = RegionCode(value = "fl"),
                        name = "Florida",
                        flag = null
                    ),
                    onMoreSelected = {

                    }
                )
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
