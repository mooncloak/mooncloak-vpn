package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.CollaboratorHeader
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.CollaboratorListItem
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.NoCollaboratorsCard
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.TipCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_description_error
import com.mooncloak.vpn.app.shared.resource.collaborator_list_title_error
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CollaboratorListScreen(
    onSendTip: () -> Unit,
    sheetState: ManagedModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createCollaboratorListComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val lazyListState = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxWidth(),
                    state = lazyListState,
                    columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (viewModel.state.current.value.collaborators.isNotEmpty()) {
                        item(
                            key = "CollaboratorListHeader",
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            CollaboratorHeader(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 32.dp)
                            )
                        }
                    }

                    if (viewModel.state.current.value.collaborators.size == 1) {
                        item(
                            key = "SingleCollaboratorItem",
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                viewModel.state.current.value.collaborators.firstOrNull()?.let { collaborator ->
                                    CollaboratorListItem(
                                        modifier = Modifier.wrapContentSize(),
                                        collaborator = collaborator
                                    )
                                }
                            }
                        }
                    } else if (viewModel.state.current.value.collaborators.isNotEmpty()) {
                        items(
                            items = viewModel.state.current.value.collaborators,
                            key = { collaborator -> collaborator.id },
                            contentType = { "CollaboratorItem" }
                        ) { collaborator ->
                            CollaboratorListItem(
                                modifier = Modifier.fillMaxWidth(),
                                collaborator = collaborator
                            )
                        }
                    }

                    if (viewModel.state.current.value.collaborators.isEmpty()) {
                        if (viewModel.state.current.value.isError) {
                            item(
                                key = "ErrorLoadingCollaboratorsItem",
                                span = StaggeredGridItemSpan.FullLine
                            ) {
                                NoCollaboratorsCard(
                                    title = stringResource(Res.string.collaborator_list_title_error),
                                    description = stringResource(Res.string.collaborator_list_description_error),
                                    icon = rememberVectorPainter(Icons.Default.Error),
                                    error = true
                                )
                            }
                        }
                    }

                    item(
                        key = "TipCard",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        TipCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
                                .padding(horizontal = DefaultHorizontalPageSpacing)
                                .padding(top = 32.dp),
                            onSendTip = onSendTip
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
}
