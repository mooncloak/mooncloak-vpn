package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.subscription.composable.SubscriptionDetailsLayout
import com.mooncloak.vpn.app.shared.feature.subscription.composable.PaymentHistoryCard
import com.mooncloak.vpn.app.shared.feature.subscription.composable.SubscriptionSection
import com.mooncloak.vpn.app.shared.feature.subscription.di.createSubscriptionComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.subscription_action_boost
import com.mooncloak.vpn.app.shared.resource.subscription_action_protect
import com.mooncloak.vpn.app.shared.resource.subscription_description_active
import com.mooncloak.vpn.app.shared.resource.subscription_description_protect
import com.mooncloak.vpn.app.shared.resource.subscription_label_payment_history
import com.mooncloak.vpn.app.shared.resource.subscription_plan_title_default
import com.mooncloak.vpn.app.shared.resource.subscription_title_active_plan
import com.mooncloak.vpn.app.shared.resource.subscription_title_no_active_plan
import org.jetbrains.compose.resources.stringResource

@Composable
public fun SubscriptionScreen(
    onOpenPlans: () -> Unit,
    onOpenPaymentHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createSubscriptionComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }

    BottomSheetLayout(
        modifier = modifier.verticalScroll(rememberScrollState()),
        title = if (viewModel.state.current.value.subscription == null) {
            stringResource(Res.string.subscription_title_no_active_plan)
        } else {
            stringResource(Res.string.subscription_title_active_plan)
        },
        description = if (viewModel.state.current.value.subscription == null) {
            stringResource(Res.string.subscription_description_protect)
        } else {
            stringResource(Res.string.subscription_description_active)
        },
        loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
    ) {
        AnimatedVisibility(
            visible = viewModel.state.current.value.details != null
        ) {
            SubscriptionDetailsLayout(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                planTitle = viewModel.state.current.value.plan?.title
                    ?: stringResource(Res.string.subscription_plan_title_default),
                subscriptionPurchased = viewModel.state.current.value.details?.purchased,
                subscriptionExpiration = viewModel.state.current.value.details?.expiration,
                subscriptionTotalData = viewModel.state.current.value.details?.totalData,
                subscriptionRemainingDuration = viewModel.state.current.value.details?.remainingDuration,
                subscriptionRemainingData = viewModel.state.current.value.details?.remainingData
            )
        }

        AnimatedContent(
            targetState = viewModel.state.current.value.lastReceipt
        ) { receipt ->
            if (receipt != null) {
                SubscriptionSection(
                    modifier = Modifier.wrapContentSize()
                        .align(Alignment.Start)
                        .padding(top = if (viewModel.state.current.value.details != null) 32.dp else 0.dp)
                        .padding(horizontal = 16.dp),
                    label = stringResource(Res.string.subscription_label_payment_history)
                ) {
                    PaymentHistoryCard(
                        modifier = Modifier.fillMaxWidth(),
                        receipt = receipt,
                        onViewAll = onOpenPaymentHistory
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    top = if (
                        viewModel.state.current.value.details != null ||
                        viewModel.state.current.value.lastReceipt != null
                    ) {
                        32.dp
                    } else {
                        0.dp
                    }
                )
                .padding(horizontal = 16.dp),
            enabled = !viewModel.state.current.value.isLoading,
            onClick = onOpenPlans
        ) {
            Text(
                text = if (viewModel.state.current.value.subscription == null) {
                    stringResource(Res.string.subscription_action_protect)
                } else {
                    stringResource(Res.string.subscription_action_boost)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
