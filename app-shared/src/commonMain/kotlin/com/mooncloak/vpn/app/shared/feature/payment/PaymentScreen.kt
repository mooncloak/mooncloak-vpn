package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.composable.PlanCard
import com.mooncloak.vpn.app.shared.feature.payment.composable.PlansLayout
import com.mooncloak.vpn.app.shared.feature.payment.di.createPaymentComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_action_select
import com.mooncloak.vpn.app.shared.resource.payment_plans_title
import org.jetbrains.compose.resources.stringResource

@Composable
public fun PaymentScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createPaymentComponent(applicationDependencies = this)
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
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = stringResource(Res.string.payment_plans_title))
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
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
                    // TODO:
                }
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
