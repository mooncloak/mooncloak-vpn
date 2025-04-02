package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.MooncloakSnackbar
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.AccountAddressCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.PercentChangeCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletActions
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletBalanceCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.vector.LunarisCoin
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_lunaris_wallet
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CryptoWalletBottomSheet(
    sheetState: ManagedModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth()
                .animateContentSize(),
            title = stringResource(Res.string.crypto_wallet_title_lunaris_wallet),
            icon = {
                Icon(
                    modifier = Modifier.padding(end = 16.dp)
                        .size(36.dp),
                    imageVector = Icons.Default.LunarisCoin,
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        ) {
            CryptoWalletScreen(
                modifier = Modifier.fillMaxWidth(),
                containerPaddingValues = PaddingValues(),
                embedded = true
            )
        }
    }
}

@Composable
public fun CryptoWalletScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues(),
    embedded: Boolean = false
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createCryptoWalletComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 28.dp + containerPaddingValues.calculateBottomPadding()),
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MooncloakSnackbar(
                        snackbarData = snackbarData
                    )
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(if (embedded) PaddingValues() else paddingValues)
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = DefaultHorizontalPageSpacing),
                state = lazyStaggeredGridState,
                columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPageSpacing),
                verticalItemSpacing = 12.dp
            ) {
                item(
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    WalletBalanceCard(
                        modifier = Modifier.fillMaxWidth(),
                        cryptoAmount = "10.000 LNRS",
                        localEstimatedAmount = "$100"
                    )
                }

                item {
                    PercentChangeCard(
                        modifier = Modifier.sizeIn(maxWidth = 150.dp)
                            .fillMaxWidth(),
                        label = "Today",
                        value = 5
                    )
                }

                item {
                    PercentChangeCard(
                        modifier = Modifier.sizeIn(maxWidth = 150.dp)
                            .fillMaxWidth(),
                        label = "All Time",
                        value = 100
                    )
                }

                item {
                    WalletActions(
                        onSend = {},
                        onReceive = {},
                        onReveal = {},
                        onRefresh = {}
                    )
                }

                item {
                    AccountAddressCard(
                        address = "123",
                        uri = "",
                        onAddressCopied = {

                        }
                    )
                }

                item(
                    key = "BottomPadding",
                    span = StaggeredGridItemSpan.FullLine
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
