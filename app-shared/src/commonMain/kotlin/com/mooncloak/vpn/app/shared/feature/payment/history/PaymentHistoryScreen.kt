package com.mooncloak.vpn.app.shared.feature.payment.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.history.composable.ErrorCard
import com.mooncloak.vpn.app.shared.feature.payment.history.composable.NoReceiptsCard
import com.mooncloak.vpn.app.shared.feature.payment.history.composable.ReceiptCard
import com.mooncloak.vpn.app.shared.feature.payment.history.di.createPaymentHistoryComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.payment_history_title
import org.jetbrains.compose.resources.stringResource

@Composable
public fun PaymentHistoryScreen(
    onGetService: () -> Unit,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createPaymentHistoryComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val lazyListState = rememberLazyListState()

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
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item(
                    key = "Title"
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp),
                        text = stringResource(Res.string.payment_history_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(
                    items = viewModel.state.current.value.receipts,
                    key = { receipt -> receipt.id },
                    contentType = { "ReceiptCard" }
                ) { receipt ->
                    ReceiptCard(
                        modifier = Modifier.fillMaxWidth()
                            .animateItem(),
                        receipt = receipt
                    )
                }

                if (viewModel.state.current.value.receipts.isEmpty() && !viewModel.state.current.value.isLoading) {
                    item(
                        key = "NoReceiptsCard"
                    ) {
                        NoReceiptsCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            onProtect = onGetService
                        )
                    }
                }

                if (!viewModel.state.current.value.errorMessage.isNullOrBlank()) {
                    item(
                        key = "ErrorCard"
                    ) {
                        ErrorCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            message = viewModel.state.current.value.errorMessage
                                ?: stringResource(Res.string.global_unexpected_error)
                        )
                    }
                }

                item(
                    key = "BottomSpacing"
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
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
