package com.mooncloak.vpn.app.shared.feature.payment.purchase

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.billing.PlanPaymentStatus
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.BitcoinInvoiceLayout
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.PaymentErrorLayout
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.PaymentSuccessLayout
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.PlansLayout
import com.mooncloak.vpn.app.shared.feature.payment.purchase.model.PaymentDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_status_pending
import org.jetbrains.compose.resources.stringResource

@Composable
public fun PaymentScreen(
    sheetState: ManagedModalBottomSheetState,
    purchasingState: PurchasingState = rememberPurchasingState(),
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createPaymentComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(viewModel.state.current.value.isPurchasing) {
        purchasingState.togglePurchasing(purchasing = viewModel.state.current.value.isPurchasing)
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize(),
            title = viewModel.state.current.value.screenTitle,
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading || viewModel.state.current.value.isPurchasing }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    targetState = viewModel.state.current.value.destination,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220, delayMillis = 90)))
                            .togetherWith(fadeOut(animationSpec = tween(90)))
                    }
                ) { destination ->
                    when (destination) {
                        PaymentDestination.Plans -> {
                            PlansLayout(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                selectedPlan = viewModel.state.current.value.selectedPlan,
                                plans = viewModel.state.current.value.plans,
                                acceptedTerms = viewModel.state.current.value.acceptedTerms,
                                loading = viewModel.state.current.value.isLoading,
                                purchasing = viewModel.state.current.value.isPurchasing,
                                noticeText = viewModel.state.current.value.noticeText,
                                termsAndConditionsText = viewModel.state.current.value.termsAndConditionsText.invoke(),
                                onPlanSelected = { plan ->
                                    viewModel.selectPlan(plan)
                                },
                                onAcceptedTermsToggled = { termsAccepted ->
                                    viewModel.toggleAcceptTerms(termsAccepted)
                                },
                                onSelect = {
                                    viewModel.purchase()
                                }
                            )
                        }

                        PaymentDestination.Invoice -> {
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

                        is PaymentDestination.PaymentError -> PaymentErrorLayout(
                            message = destination.message,
                            modifier = Modifier.fillMaxWidth()
                        )

                        PaymentDestination.PaymentSuccess -> PaymentSuccessLayout(
                            modifier = Modifier.fillMaxWidth()
                        )

                        null -> {}
                    }
                }
            }
        }
    }
}
