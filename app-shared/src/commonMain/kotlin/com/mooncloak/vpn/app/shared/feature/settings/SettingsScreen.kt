package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.composable.rememberModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberDependency
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsAppGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsBottomSheet
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsFooterItem
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsLegalGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsPreferenceGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsSubscriptionGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsThemeGroup
import com.mooncloak.vpn.app.shared.feature.settings.di.createSettingsComponent
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_built_description
import com.mooncloak.vpn.app.shared.resource.destination_main_settings_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalPersistentStateAPI::class)
@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createSettingsComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val bottomSheetState = rememberModalNavigationBottomSheetState<SettingsBottomSheetDestination>()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val preferencesStorage = rememberDependency { preferencesStorage }

    LaunchedEffect(Unit) {
        viewModel.load()
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
                    Text(text = stringResource(Res.string.destination_main_settings_title))
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(containerPaddingValues.calculateTopPadding()))

                SettingsSubscriptionGroup(
                    currentPlan = viewModel.state.current.value.currentPlan,
                    onOpenSubscription = {
                        coroutineScope.launch {
                            bottomSheetState.show(SettingsBottomSheetDestination.Subscription)
                        }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                SettingsThemeGroup(
                    themePreference = preferencesStorage.theme.current.value,
                    onThemePreferenceValueChanged = { preference ->
                        coroutineScope.launch {
                            preferencesStorage.theme.update(preference)
                        }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                SettingsAppGroup(
                    appVersion = viewModel.state.current.value.appVersion,
                    sourceCodeUri = viewModel.state.current.value.sourceCodeUri,
                    onOpenDependencyList = {
                        coroutineScope.launch {
                            bottomSheetState.show(SettingsBottomSheetDestination.DependencyLicenseList)
                        }
                    },
                    onOpenCollaboratorList = {
                        coroutineScope.launch {
                            bottomSheetState.show(SettingsBottomSheetDestination.Collaborators)
                        }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                SettingsPreferenceGroup(
                    startOnLandingScreen = viewModel.state.current.value.startOnLandingScreen,
                    isSystemAuthSupported = viewModel.state.current.value.isSystemAuthSupported,
                    requireSystemAuth = viewModel.state.current.value.requireSystemAuth,
                    systemAuthTimeout = viewModel.state.current.value.systemAuthTimeout,
                    onToggleStartOnLandingScreen = { checked ->
                        viewModel.toggleStartOnLandingScreen(checked)
                    },
                    onToggleRequireSystemAuth = { checked ->
                        viewModel.toggleRequireSystemAuth(checked)
                    },
                    onSystemAuthTimeoutChanged = { timeout ->
                        viewModel.updateSystemAuthTimeout(timeout)
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                SettingsLegalGroup(
                    privacyPolicyUri = viewModel.state.current.value.privacyPolicyUri,
                    termsUri = viewModel.state.current.value.termsUri
                )

                SettingsFooterItem(
                    modifier = Modifier.fillMaxWidth(),
                    copyright = viewModel.state.current.value.copyright,
                    description = stringResource(Res.string.app_built_description)
                )

                Spacer(modifier = Modifier.height(containerPaddingValues.calculateBottomPadding() + 28.dp))
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewModel.state.current.value.isLoading
            ) {
                CircularProgressIndicator()
            }
        }
    }

    SettingsBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        state = bottomSheetState
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
