package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.MooncloakSnackbar
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
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
            title = stringResource(Res.string.crypto_wallet_title_lunaris_wallet)
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
