package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.layout.CountryListLayout
import com.mooncloak.vpn.app.shared.feature.country.layout.RegionListLayout
import com.mooncloak.vpn.app.shared.feature.country.layout.ServerListLayout
import com.mooncloak.vpn.app.shared.feature.country.model.CountryListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.RegionListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.ServerListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.label
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_back
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CountryListScreen(
    sheetState: ManagedModalBottomSheetState,
    onOpenPlans: () -> Unit,
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

    val countryLazyListState = rememberLazyListState()
    val regionLazyListState = rememberLazyListState()
    val serverLazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(viewModel.state.current.value.hideSheet) {
        if (viewModel.state.current.value.hideSheet) {
            sheetState.hide()
        }
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = viewModel.state.current.value.title,
            description = viewModel.state.current.value.description,
            icon = {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (viewModel.state.current.value.isBackSupported) {
                        Icon(
                            modifier = Modifier.padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.goBack() }
                                .padding(8.dp),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_action_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    viewModel.state.current.value.icon()
                }
            },
            snackbarHostState = snackbarHostState,
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
        ) {
            AnimatedContent(
                targetState = viewModel.state.current.value.layout,
                transitionSpec = {
                    if (targetState::class != initialState::class) {
                        (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    } else {
                        EnterTransition.None togetherWith ExitTransition.None
                    }
                },
                label = "CountryListLayoutTransition"
            ) { layout ->
                when (layout) {
                    is CountryListLayoutStateModel -> CountryListLayout(
                        connection = viewModel.state.current.value.connection,
                        countries = layout.countries,
                        label = layout.label,
                        loading = viewModel.state.current.value.isLoading,
                        canAppendMore = viewModel.state.current.value.canAppendMore,
                        lazyListState = countryLazyListState,
                        errorTitle = null,
                        errorDescription = null,
                        onLoadMore = viewModel::loadMore,
                        onConnect = { details ->
                            if (viewModel.state.current.value.isMember) {
                                viewModel.connectTo(country = details.country)
                            } else {
                                onOpenPlans.invoke()
                            }
                        },
                        onDetails = viewModel::goTo
                    )

                    is RegionListLayoutStateModel -> RegionListLayout(
                        connection = viewModel.state.current.value.connection,
                        regions = layout.countryDetails.regions,
                        label = layout.label,
                        loading = viewModel.state.current.value.isLoading,
                        lazyListState = regionLazyListState,
                        errorTitle = null,
                        errorDescription = null,
                        onConnect = { details ->
                            if (viewModel.state.current.value.isMember) {
                                viewModel.connectTo(region = details.region)
                            } else {
                                onOpenPlans.invoke()
                            }
                        },
                        onDetails = { details ->
                            viewModel.goTo(country = layout.countryDetails, region = details)
                        }
                    )

                    is ServerListLayoutStateModel -> ServerListLayout(
                        connection = viewModel.state.current.value.connection,
                        servers = layout.servers,
                        label = layout.label,
                        loading = viewModel.state.current.value.isLoading,
                        canAppendMore = viewModel.state.current.value.canAppendMore,
                        lazyListState = serverLazyListState,
                        errorTitle = null,
                        errorDescription = null,
                        onLoadMore = viewModel::loadMore,
                        onConnect = { server ->
                            if (viewModel.state.current.value.isMember) {
                                viewModel.connectTo(server)
                            } else {
                                onOpenPlans.invoke()
                            }
                        }
                    )
                }
            }
        }
    }
}
