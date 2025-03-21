package com.mooncloak.vpn.app.shared.feature.collaborator.tip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.tip.composable.TipLinkItemCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_description
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_title
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import org.jetbrains.compose.resources.stringResource

@Composable
public fun TipScreen(
    sheetState: ManagedModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createTipComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyStaggeredGridState()
    val uriHandler = LocalUriHandler.current

    val accentColors = remember {
        listOf(
            ColorPalette.Blue_500 to Color.White,
            ColorPalette.Teal_500 to Color.White,
            ColorPalette.Purple_600 to Color.White,
            ColorPalette.Pink_500 to Color.White
        )
    }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading },
            snackbarHostState = snackbarHostState,
            title = stringResource(Res.string.collaborator_list_tip_title),
            description = stringResource(Res.string.collaborator_list_tip_description)
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = DefaultHorizontalPageSpacing),
                state = lazyListState,
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp
            ) {
                itemsIndexed(
                    items = viewModel.state.current.value.items,
                    key = { _, link -> link.url },
                    contentType = { _, _ -> "TipLinkItem" }
                ) { index, link ->
                    TipLinkItemCard(
                        modifier = Modifier.sizeIn(
                            minHeight = 280.dp,
                            maxWidth = 400.dp
                        ).fillMaxWidth(),
                        item = link,
                        onSelected = {
                            uriHandler.openUri(link.url)
                        },
                        containerColor = accentColors[index % accentColors.size].first,
                        contentColor = accentColors[index % accentColors.size].second
                    )
                }

                item(
                    key = "BottomSpacing",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
