package com.mooncloak.vpn.app.shared.feature.payment.purchase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.app.shared.api.billing.PlanPaymentStatus
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.BitcoinInvoiceLayout
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.PlansLayout
import com.mooncloak.vpn.app.shared.feature.payment.purchase.di.createPaymentComponent
import com.mooncloak.vpn.app.shared.feature.payment.purchase.model.PaymentDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_status_pending
import org.jetbrains.compose.resources.stringResource

@Composable
public fun PaymentScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createPaymentComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent,
            navController = navController
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.state.current.value.startDestination?.let { startDestination ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    viewModel.state.current.value.screenTitle?.let { screenTitle ->
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = screenTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    NavHost(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 16.dp),
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable<PaymentDestination.Plans> {
                            PlansLayout(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                selectedPlan = viewModel.state.current.value.selectedPlan,
                                plans = viewModel.state.current.value.plans,
                                acceptedTerms = viewModel.state.current.value.acceptedTerms,
                                loading = viewModel.state.current.value.isLoading,
                                noticeText = viewModel.state.current.value.noticeText,
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
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .verticalScroll(scrollState),
                                uri = viewModel.state.current.value.invoice?.uri ?: "",
                                paymentStatusTitle = viewModel.state.current.value.paymentStatus?.title
                                    ?: stringResource(Res.string.payment_status_pending),
                                paymentStatusDescription = viewModel.state.current.value.paymentStatus?.description,
                                paymentStatusPending = viewModel.state.current.value.paymentStatus is PlanPaymentStatus.Pending,
                                address = viewModel.state.current.value.invoice?.address
                            )
                        }
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
}
