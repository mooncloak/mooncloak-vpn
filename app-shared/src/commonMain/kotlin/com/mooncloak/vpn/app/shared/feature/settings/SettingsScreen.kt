package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import com.mooncloak.vpn.app.shared.di.rememberApplicationDependency
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsBottomSheet
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsGroupLabel
import com.mooncloak.vpn.app.shared.feature.settings.composable.ThemePreferenceSegmentedButton
import com.mooncloak.vpn.app.shared.feature.settings.di.createSettingsComponent
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.destination_main_settings_title
import com.mooncloak.vpn.app.shared.resource.settings_group_legal
import com.mooncloak.vpn.app.shared.resource.settings_group_theme
import com.mooncloak.vpn.app.shared.resource.settings_title_code
import com.mooncloak.vpn.app.shared.resource.settings_title_licenses
import com.mooncloak.vpn.app.shared.resource.settings_title_privacy_policy
import com.mooncloak.vpn.app.shared.resource.settings_title_terms
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalPersistentStateAPI::class)
@Composable
public fun SettingsScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createSettingsComponent(applicationDependencies = this)
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val bottomSheetState = rememberModalNavigationBottomSheetState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    val preferencesStorage = rememberApplicationDependency { preferencesStorage }

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

                SettingsGroupLabel(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(top = 32.dp),
                    text = stringResource(Res.string.settings_group_theme)
                )

                ThemePreferenceSegmentedButton(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(top = 16.dp),
                    themePreference = preferencesStorage.theme.current.value ?: ThemePreference.System,
                    onThemePreferenceSelected = { preference ->
                        coroutineScope.launch {
                            preferencesStorage.theme.update(preference)
                        }
                    }
                )

                SettingsGroupLabel(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(top = 32.dp),
                    text = stringResource(Res.string.settings_group_legal)
                )

                viewModel.state.current.value.privacyPolicyUri?.let { privacyPolicyUri ->
                    ListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                uriHandler.openUri(privacyPolicyUri)
                            },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        headlineContent = {
                            Text(text = stringResource(Res.string.settings_title_privacy_policy))
                        }
                    )
                }

                viewModel.state.current.value.termsUri?.let { termsUri ->
                    ListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                uriHandler.openUri(termsUri)
                            },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        headlineContent = {
                            Text(text = stringResource(Res.string.settings_title_terms))
                        }
                    )
                }

                ListItem(
                    modifier = Modifier.fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                bottomSheetState.show(SettingsBottomSheetDestination.DependencyLicenseList)
                            }
                        },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    headlineContent = {
                        Text(text = stringResource(Res.string.settings_title_licenses))
                    }
                )

                viewModel.state.current.value.sourceCodeUri?.let { sourceCodeUri ->
                    ListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                uriHandler.openUri(sourceCodeUri)
                            },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        headlineContent = {
                            Text(text = stringResource(Res.string.settings_title_code))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(containerPaddingValues.calculateBottomPadding()))
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
