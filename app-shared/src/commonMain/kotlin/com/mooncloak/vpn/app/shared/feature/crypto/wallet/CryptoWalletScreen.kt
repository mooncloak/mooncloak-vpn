package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.MooncloakSnackbar
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.showError
import com.mooncloak.vpn.app.shared.composable.showSuccess
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.AccountAddressCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.AmountChangeContainer
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.GiftCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.NoWalletCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.PromoCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletActions
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletBalanceCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletDetailsCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout.ReceivePaymentLayout
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.vector.LunarisCoin
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_message_address_copied
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_lunaris_wallet
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import com.mooncloak.vpn.crypto.lunaris.model.uri
import com.mooncloak.vpn.util.shared.time.DateTimeFormatter
import com.mooncloak.vpn.util.shared.time.Full
import com.mooncloak.vpn.util.shared.time.format
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

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
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val dateTimeFormatter = remember { DateTimeFormatter.Full }

    val createWalletBottomSheetState = rememberManagedModalBottomSheetState()
    val restoreWalletBottomSheetState = rememberManagedModalBottomSheetState()
    val sendPaymentBottomSheetState = rememberManagedModalBottomSheetState()
    val receivePaymentBottomSheetState = rememberManagedModalBottomSheetState(skipPartiallyExpanded = true)
    val revealSeedPhraseBottomSheetState = rememberManagedModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
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
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = topAppBarBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Default.LunarisCoin,
                            contentDescription = null,
                            tint = Color.Unspecified
                        )

                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(Res.string.crypto_wallet_title_lunaris_wallet)
                        )
                    }
                }
            )
        }
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
                if (viewModel.state.current.value.wallet != null) {
                    item(
                        key = "WalletBalanceCard",
                        contentType = "WalletBalanceCard",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        WalletBalanceCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            cryptoAmount = viewModel.state.current.value.balance?.amount?.formatted,
                            localEstimatedAmount = viewModel.state.current.value.balance?.localEstimate?.formatted
                        )
                    }
                }

                viewModel.state.current.value.promo?.let { promoDetails ->
                    item(
                        key = "PromoDetails",
                        contentType = "PromoCard",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        PromoCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            title = promoDetails.title,
                            description = promoDetails.description,
                            icon = promoDetails.icon.invoke()
                        )
                    }
                }

                if (viewModel.state.current.value.showNoWalletCard) {
                    item(
                        key = "NoWalletCard",
                        contentType = "NoWalletCard",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        NoWalletCard(
                            modifier = Modifier.sizeIn(
                                minWidth = 300.dp
                            ).fillMaxWidth()
                                .animateItem(),
                            onCreateWallet = {
                                coroutineScope.launch {
                                    createWalletBottomSheetState.show()
                                }
                            },
                            onRestoreWallet = {
                                coroutineScope.launch {
                                    restoreWalletBottomSheetState.show()
                                }
                            }
                        )
                    }
                }

                viewModel.state.current.value.stats?.let { stats ->
                    item(
                        key = "WalletStats",
                        contentType = "WalletStats"
                    ) {
                        AmountChangeContainer(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            today = stats.dailyChange?.value,
                            allTime = stats.allTimeChange?.value
                        )
                    }
                }

                item(
                    key = "WalletActions",
                    contentType = "WalletActions",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    WalletActions(
                        modifier = Modifier.fillMaxWidth()
                            .animateItem(),
                        sendEnabled = viewModel.state.current.value.sendEnabled,
                        receiveEnabled = viewModel.state.current.value.receiveEnabled,
                        revealEnabled = viewModel.state.current.value.revealEnabled,
                        onSend = {
                            coroutineScope.launch {
                                sendPaymentBottomSheetState.show()
                            }
                        },
                        onReceive = {
                            coroutineScope.launch {
                                receivePaymentBottomSheetState.show()
                            }
                        },
                        onReveal = {
                            coroutineScope.launch {
                                revealSeedPhraseBottomSheetState.show()
                            }
                        },
                        onRefresh = viewModel::refresh
                    )
                }

                viewModel.state.current.value.wallet?.let { wallet ->
                    item(
                        key = "AccountAddressCard",
                        contentType = "AccountAddressCard",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        AccountAddressCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            address = wallet.address,
                            uri = wallet.uri(),
                            onAddressCopied = {
                                coroutineScope.launch {
                                    snackbarHostState.showSuccess(
                                        notification = NotificationStateModel(
                                            message = getString(Res.string.crypto_wallet_message_address_copied)
                                        )
                                    )
                                }
                            }
                        )
                    }
                }

                item(
                    key = "WalletDetailsCard",
                    contentType = "WalletDetailsCard",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    WalletDetailsCard(
                        modifier = Modifier.fillMaxWidth()
                            .animateItem(),
                        blockchain = viewModel.state.current.value.blockChain,
                        network = viewModel.state.current.value.network,
                        tokenName = viewModel.state.current.value.wallet?.currency?.name,
                        tokenTicker = viewModel.state.current.value.wallet?.currency?.ticker,
                        address = viewModel.state.current.value.wallet?.address,
                        amount = viewModel.state.current.value.balance?.amount?.formatted,
                        estimatedValue = viewModel.state.current.value.balance?.localEstimate?.formatted,
                        lastUpdated = viewModel.state.current.value.timestamp?.let { dateTimeFormatter.format(it) }
                    )
                }

                item(
                    key = "BottomSpacing",
                    contentType = "Spacing",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Spacer(modifier = Modifier.height(28.dp + containerPaddingValues.calculateBottomPadding()))
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

    LaunchedEffect(viewModel.state.current.value.error) {
        viewModel.state.current.value.error?.let { error ->
            snackbarHostState.showError(error)
        }
    }

    LaunchedEffect(viewModel.state.current.value.success) {
        viewModel.state.current.value.success?.let { success ->
            snackbarHostState.showSuccess(success)
        }
    }

    ReceivePaymentLayout(
        modifier = Modifier.fillMaxWidth(),
        address = viewModel.state.current.value.wallet?.address ?: "",
        uri = viewModel.state.current.value.wallet?.uri() ?: "",
        sheetState = receivePaymentBottomSheetState
    )
}
