package com.mooncloak.vpn.app.shared.feature.subscription

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.subscription.composable.ActiveSubscriptionLayout
import com.mooncloak.vpn.app.shared.feature.subscription.di.createSubscriptionComponent

@Composable
public fun SubscriptionScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createSubscriptionComponent(presentationDependencies = this)
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            // TODO: Subscription UI

            /*
            NoActiveSubscriptionLayout(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onProtect = {

                }
            )*/

            ActiveSubscriptionLayout(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                planTitle = "The One Day Plan",
                planDescription = "Access to a private and secure VPN for one day.",
                subscriptionPurchased = "Today",
                subscriptionExpiration = "Tomorrow",
                subscriptionTotalData = "1 Gb",
                subscriptionRemainingDuration = "< 1 Day",
                subscriptionRemainingData = "500 Mb",
                lastPaymentTitle = "The One Day Plan - $5",
                lastPaymentDescription = "Purchased yesterday",
                onBoost = {},
                onViewPaymentHistory = {}
            )

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
