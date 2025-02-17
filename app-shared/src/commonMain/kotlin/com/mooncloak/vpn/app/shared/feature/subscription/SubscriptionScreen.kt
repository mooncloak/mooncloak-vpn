package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberDependency
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.subscription.composable.ActiveSubscriptionLayout
import com.mooncloak.vpn.app.shared.feature.subscription.composable.NoActiveSubscriptionLayout
import com.mooncloak.vpn.app.shared.feature.subscription.di.createSubscriptionComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
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

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            val plan = viewModel.state.current.value.plan
            val usage = viewModel.state.current.value.usage

            AnimatedContent(
                targetState = viewModel.state.current.value.subscription
            ) { subscription ->
                when (subscription) {
                    null -> NoActiveSubscriptionLayout(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        lastPaymentTitle = null,
                        lastPaymentDescription = null,
                        onViewPaymentHistory = {
                            // TODO: Open payment history
                        },
                        onProtect = onOpenPlans
                    )

                    else -> ActiveSubscriptionLayout(
                        modifier = Modifier.fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        planTitle = plan?.title ?: stringResource(Res.string.global_not_available),
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
                        onViewPaymentHistory = {
                            // TODO: Open payment history
                        },
                        onBoost = onOpenPlans
                    )
                }
            }

            SnackbarHost(hostState = snackbarHostState)

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
