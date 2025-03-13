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
import androidx.compose.material3.SheetValue
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
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.list.CollaboratorListScreen
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListScreen
import com.mooncloak.vpn.app.shared.feature.payment.history.PaymentHistoryScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.rememberPurchasingState
import com.mooncloak.vpn.app.shared.feature.settings.composable.AppDetailsBottomSheetLayout
import com.mooncloak.vpn.app.shared.feature.settings.composable.DeviceDetailsBottomSheetLayout
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsAppGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsDeviceGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsFooterItem
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsLegalGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsPreferenceGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsSubscriptionGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsThemeGroup
import com.mooncloak.vpn.app.shared.feature.settings.composable.SettingsWireGuardGroup
import com.mooncloak.vpn.app.shared.feature.subscription.SubscriptionScreen
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerConfigScreen
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_built_description
import com.mooncloak.vpn.app.shared.resource.destination_main_settings_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

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

    val paymentPurchasingState = rememberPurchasingState()
    val paymentBottomSheetState = rememberManagedModalBottomSheetState(
        confirmValueChange = { value ->
            when (value) {
                // Disable closing the bottom sheet when we are purchasing to prevent getting in an invalid state.
                SheetValue.Hidden -> !paymentPurchasingState.purchasing.value
                SheetValue.Expanded -> true
                SheetValue.PartiallyExpanded -> true
            }
        }
    )

    val subscriptionBottomSheetState = rememberManagedModalBottomSheetState()
    val paymentHistoryBottomSheetState = rememberManagedModalBottomSheetState()
    val dependencyListBottomSheetState = rememberManagedModalBottomSheetState()
    val appDetailsBottomSheetState = rememberManagedModalBottomSheetState()
    val deviceDetailsBottomSheetState = rememberManagedModalBottomSheetState()
    val dnsServerConfigBottomSheetState = rememberManagedModalBottomSheetState(skipPartiallyExpanded = true)
    val collaboratorListBottomSheetState = rememberManagedModalBottomSheetState()

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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
                            subscriptionBottomSheetState.show()
                        }
                    },
                    onOpenTransactionHistory = {
                        coroutineScope.launch {
                            paymentHistoryBottomSheetState.show()
                        }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                SettingsThemeGroup(
                    themePreference = viewModel.state.current.value.themePreference,
                    onThemePreferenceValueChanged = viewModel::updateThemePreference
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                SettingsAppGroup(
                    appVersion = viewModel.state.current.value.appDetails?.version,
                    sourceCodeUri = viewModel.state.current.value.sourceCodeUri,
                    appDetailsEnabled = viewModel.state.current.value.appDetails != null,
                    onOpenAppDetails = {
                        viewModel.state.current.value.appDetails?.let { details ->
                            coroutineScope.launch {
                                appDetailsBottomSheetState.show()
                            }
                        }
                    },
                    onOpenDependencyList = {
                        coroutineScope.launch {
                            dependencyListBottomSheetState.show()
                        }
                    },
                    onOpenCollaboratorList = {
                        coroutineScope.launch {
                            collaboratorListBottomSheetState.show()
                        }
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                )

                if (viewModel.state.current.value.deviceDetails != null) {
                    SettingsDeviceGroup(
                        deviceDetailsEnabled = viewModel.state.current.value.deviceDetails != null,
                        onOpenDeviceDetails = {
                            viewModel.state.current.value.deviceDetails?.let { details ->
                                coroutineScope.launch {
                                    deviceDetailsBottomSheetState.show()
                                }
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                    )
                }

                if (viewModel.state.current.value.wireGuardPreferences != null) {
                    SettingsWireGuardGroup(
                        dnsServersEnabled = viewModel.state.current.value.wireGuardPreferences != null,
                        onOpenDnsServers = {
                            viewModel.state.current.value.wireGuardPreferences?.let {
                                coroutineScope.launch {
                                    dnsServerConfigBottomSheetState.show()
                                }
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = SecondaryAlpha)
                    )
                }

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
                    aboutUri = viewModel.state.current.value.aboutUri,
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

    SubscriptionScreen(
        sheetState = subscriptionBottomSheetState,
        modifier = Modifier.fillMaxWidth(),
        onOpenPlans = {
            coroutineScope.launch {
                paymentBottomSheetState.show()
            }
        },
        onOpenPaymentHistory = {
            coroutineScope.launch {
                paymentHistoryBottomSheetState.show()
            }
        }
    )

    PaymentHistoryScreen(
        sheetState = paymentHistoryBottomSheetState,
        modifier = Modifier.fillMaxWidth(),
        onGetService = {
            coroutineScope.launch {
                paymentBottomSheetState.show()
            }
        }
    )

    DependencyLicenseListScreen(
        sheetState = dependencyListBottomSheetState,
        modifier = Modifier.fillMaxWidth()
    )

    AppDetailsBottomSheetLayout(
        sheetState = appDetailsBottomSheetState,
        modifier = Modifier.fillMaxWidth(),
        details = viewModel.state.current.value.appDetails
    )

    PaymentScreen(
        modifier = Modifier.fillMaxWidth(),
        sheetState = paymentBottomSheetState,
        purchasingState = paymentPurchasingState
    )

    DeviceDetailsBottomSheetLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = deviceDetailsBottomSheetState,
        details = viewModel.state.current.value.deviceDetails
    )

    DnsServerConfigScreen(
        modifier = Modifier.fillMaxWidth(),
        sheetState = dnsServerConfigBottomSheetState,
        onSave = {
            coroutineScope.launch {
                dnsServerConfigBottomSheetState.hide()
            }
        }
    )

    CollaboratorListScreen(
        modifier = Modifier.fillMaxWidth(),
        sheetState = collaboratorListBottomSheetState
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
