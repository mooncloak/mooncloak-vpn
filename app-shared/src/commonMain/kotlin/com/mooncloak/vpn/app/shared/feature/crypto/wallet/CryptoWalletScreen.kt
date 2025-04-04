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
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.NoWalletCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.PromoCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletActions
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletBalanceCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletDetailsCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout.CreateWalletLayout
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout.ReceivePaymentLayout
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout.RestoreWalletLayout
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout.RevealSeedPhraseLayout
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout.SendPaymentLayout
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletFeedItem
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.vector.LunarisCoin
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_message_address_copied
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_lunaris_wallet
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import com.mooncloak.vpn.crypto.lunaris.model.uri
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

    val createWalletBottomSheetState = rememberManagedModalBottomSheetState()
    val restoreWalletBottomSheetState = rememberManagedModalBottomSheetState()
    val sendPaymentBottomSheetState = rememberManagedModalBottomSheetState(skipPartiallyExpanded = true)
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
                items(
                    items = viewModel.state.current.value.items,
                    key = { item -> item.key },
                    contentType = { item -> item.contentType },
                    span = { item -> item.span }
                ) { item ->
                    when (item) {
                        is WalletFeedItem.AccountAddressItem -> AccountAddressCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            address = item.wallet.address,
                            uri = item.wallet.uri(),
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

                        WalletFeedItem.ActionsItem -> WalletActions(
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

                        is WalletFeedItem.BalanceItem -> WalletBalanceCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            cryptoAmount = item.balance?.amount?.formatted,
                            localEstimatedAmount = item.balance?.localEstimate?.formatted
                        )

                        is WalletFeedItem.DetailsItem -> WalletDetailsCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            blockchain = item.blockChain,
                            network = item.network,
                            tokenName = item.wallet?.currency?.name,
                            tokenTicker = item.wallet?.currency?.ticker,
                            tokenAddress = item.wallet?.currency?.address,
                            walletAddress = item.wallet?.address,
                            amount = item.balance?.amount?.formatted,
                            estimatedValue = item.balance?.localEstimate?.formatted,
                            lastUpdated = item.timestamp
                        )

                        WalletFeedItem.NoWalletItem -> NoWalletCard(
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

                        is WalletFeedItem.PromoItem -> PromoCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            title = item.details.title,
                            description = item.details.description,
                            icon = item.details.icon.invoke()
                        )

                        is WalletFeedItem.StatsItem -> AmountChangeContainer(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            today = item.stats?.dailyChange?.value,
                            allTime = item.stats?.allTimeChange?.value
                        )
                    }
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

    SendPaymentLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = sendPaymentBottomSheetState,
        balance = viewModel.state.current.value.balance,
        model = viewModel.state.current.value.send,
        onAddressChanged = viewModel::updateAddress,
        onAmountChanged = viewModel::updateAmount,
        onSend = viewModel::sendPayment
    )

    ReceivePaymentLayout(
        modifier = Modifier.fillMaxWidth(),
        address = viewModel.state.current.value.wallet?.address ?: "",
        uri = viewModel.state.current.value.wallet?.uri() ?: "",
        sheetState = receivePaymentBottomSheetState
    )

    RevealSeedPhraseLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = revealSeedPhraseBottomSheetState
    )

    CreateWalletLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = createWalletBottomSheetState
    )

    RestoreWalletLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = restoreWalletBottomSheetState
    )
}
