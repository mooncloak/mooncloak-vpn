package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.composable.BitcoinInvoiceLayout
import com.mooncloak.vpn.app.shared.feature.payment.composable.PlansLayout
import com.mooncloak.vpn.app.shared.feature.payment.di.createPaymentComponent
import com.mooncloak.vpn.app.shared.feature.payment.model.PaymentDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_plans_title
import com.mooncloak.vpn.app.shared.resource.payment_status_pending
import org.jetbrains.compose.resources.stringResource

@Composable
public fun PaymentScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createPaymentComponent(
            applicationDependencies = this,
            navController = navController
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            viewModel.state.current.value.screenTitle?.let { screenTitle ->
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(text = screenTitle)
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            viewModel.state.current.value.startDestination?.let { startDestination ->
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable<PaymentDestination.Plans> {
                        PlansLayout(
                            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = 12.dp),
                            selectedPlan = viewModel.state.current.value.selectedPlan,
                            plans = viewModel.state.current.value.plans,
                            acceptedTerms = viewModel.state.current.value.acceptedTerms,
                            termsAndConditionsText = viewModel.state.current.value.termsAndConditionsText.invoke(),
                            onPlanSelected = { plan ->
                                viewModel.selectPlan(plan)
                            },
                            onAcceptedTermsToggled = { termsAccepted ->
                                viewModel.toggleAcceptTerms(termsAccepted)
                            },
                            onSelect = {
                                viewModel.createInvoice()
                            }
                        )
                    }
                    composable<PaymentDestination.Invoice> {
                        BitcoinInvoiceLayout(
                            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .verticalScroll(scrollState),
                            uri = viewModel.state.current.value.invoice?.uri ?: "",
                            paymentStatusTitle = viewModel.state.current.value.paymentStatus?.title
                                ?: stringResource(Res.string.payment_status_pending),
                            paymentStatusDescription = viewModel.state.current.value.paymentStatus?.description,
                            address = viewModel.state.current.value.invoice?.address
                        )
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewModel.state.current.value.isLoading
            ) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
