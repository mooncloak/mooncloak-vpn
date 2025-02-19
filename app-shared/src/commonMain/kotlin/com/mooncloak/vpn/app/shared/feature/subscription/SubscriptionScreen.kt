package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberDependency
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.subscription.composable.ActiveSubscriptionLayout
import com.mooncloak.vpn.app.shared.feature.subscription.di.createSubscriptionComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.subscription_action_protect
import com.mooncloak.vpn.app.shared.resource.subscription_description_active
import com.mooncloak.vpn.app.shared.resource.subscription_description_protect
import com.mooncloak.vpn.app.shared.resource.subscription_plan_title_default
import com.mooncloak.vpn.app.shared.resource.subscription_title_active_plan
import com.mooncloak.vpn.app.shared.resource.subscription_title_no_active_plan
import com.mooncloak.vpn.app.shared.util.DataFormatter
import com.mooncloak.vpn.app.shared.util.Default
import com.mooncloak.vpn.app.shared.util.time.DateTimeFormatter
import com.mooncloak.vpn.app.shared.util.time.Default
import com.mooncloak.vpn.app.shared.util.time.DurationFormatter
import com.mooncloak.vpn.app.shared.util.time.Full
import com.mooncloak.vpn.app.shared.util.time.format
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
    val dateTimeFormatter = remember { DateTimeFormatter.Full }
    val dataFormatter = remember { DataFormatter.Default }
    val durationFormatter = remember { DurationFormatter.Default }
    val clock = rememberDependency { clock }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }

    BottomSheetLayout(
        modifier = modifier,
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
        val plan = viewModel.state.current.value.plan
        val usage = viewModel.state.current.value.usage

        Box(modifier = Modifier.fillMaxWidth()) {
            AnimatedContent(
                modifier = Modifier.fillMaxWidth(),
                targetState = viewModel.state.current.value.subscription
            ) { subscription ->
                when {
                    // This is done so that we don't have a janky UI transition between the no plan and active plan
                    // states every time the screen is loaded. Instead, we display a blank loading screen, while we are
                    // retrieving the first data.
                    subscription == null && viewModel.state.current.value.isLoading -> {
                        Box(modifier = Modifier.sizeIn(minHeight = 250.dp))
                    }

                    subscription == null -> {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onOpenPlans
                        ) {
                            Text(
                                text = stringResource(Res.string.subscription_action_protect)
                            )
                        }
                    }

                    else -> ActiveSubscriptionLayout(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        planTitle = plan?.title ?: stringResource(Res.string.subscription_plan_title_default),
                        planDescription = plan?.description?.value,
                        subscriptionPurchased = dateTimeFormatter.format(subscription.created),
                        subscriptionExpiration = dateTimeFormatter.format(subscription.expiration),
                        subscriptionTotalData = (subscription.totalThroughput)?.let { bytes ->
                            dataFormatter.format(
                                value = bytes,
                                inputType = DataFormatter.Type.Bytes,
                                outputType = DataFormatter.Type.Megabytes
                            )
                        },
                        subscriptionRemainingDuration = durationFormatter.format(
                            usage?.durationRemaining ?: (subscription.expiration - clock.now())
                        ),
                        subscriptionRemainingData = usage?.totalThroughputRemaining?.let { bytes ->
                            dataFormatter.format(
                                value = bytes,
                                inputType = DataFormatter.Type.Bytes,
                                outputType = DataFormatter.Type.Megabytes
                            )
                        },
                        lastPaymentTitle = null,
                        lastPaymentDescription = null,
                        onViewPaymentHistory = onOpenPaymentHistory,
                        onBoost = onOpenPlans
                    )
                }
            }
        }
    }
}
