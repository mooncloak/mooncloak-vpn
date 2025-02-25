package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.CollaboratorHeader
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.CollaboratorListItem
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.NoCollaboratorsCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_description_empty
import com.mooncloak.vpn.app.shared.resource.collaborator_list_description_error
import com.mooncloak.vpn.app.shared.resource.collaborator_list_title_empty
import com.mooncloak.vpn.app.shared.resource.collaborator_list_title_error
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CollaboratorListScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createCollaboratorListComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val lazyListState = rememberLazyGridState()

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
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState,
                columns = GridCells.Adaptive(minSize = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (viewModel.state.current.value.collaborators.isNotEmpty()) {
                    item(
                        key = "CollaboratorListHeader",
                        span = { GridItemSpan(currentLineSpan = maxLineSpan) }
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
                        span = { GridItemSpan(currentLineSpan = maxLineSpan) }
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
                            span = { GridItemSpan(currentLineSpan = maxLineSpan) }
                        ) {
                            NoCollaboratorsCard(
                                title = stringResource(Res.string.collaborator_list_title_error),
                                description = stringResource(Res.string.collaborator_list_description_error),
                                icon = rememberVectorPainter(Icons.Default.Error),
                                error = true
                            )
                        }
                    } else {
                        item(
                            key = "NoCollaboratorsItem",
                            span = { GridItemSpan(currentLineSpan = maxLineSpan) }
                        ) {
                            NoCollaboratorsCard(
                                title = stringResource(Res.string.collaborator_list_title_empty),
                                description = stringResource(Res.string.collaborator_list_description_empty),
                                icon = rememberVectorPainter(Icons.Default.Search),
                                error = false
                            )
                        }
                    }
                }

                item(
                    key = "BottomSpacing",
                    span = { GridItemSpan(currentLineSpan = maxLineSpan) }
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
