package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.CollaboratorListItem
import com.mooncloak.vpn.app.shared.feature.collaborator.list.composable.NoCollaboratorsCard
import com.mooncloak.vpn.app.shared.feature.collaborator.list.di.createCollaboratorListComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_description_empty
import com.mooncloak.vpn.app.shared.resource.collaborator_list_description_error
import com.mooncloak.vpn.app.shared.resource.collaborator_list_header
import com.mooncloak.vpn.app.shared.resource.collaborator_list_title_empty
import com.mooncloak.vpn.app.shared.resource.collaborator_list_title_error
import org.jetbrains.compose.resources.stringResource

@Composable
public fun CollaboratorListScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createCollaboratorListComponent(applicationDependencies = this)
    }
    val viewModel = remember { componentDependencies.viewModel }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState
            ) {
                if (viewModel.state.current.value.collaborators.isNotEmpty()) {
                    item(key = "CollaboratorListHeader") {
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp),
                            text = stringResource(Res.string.collaborator_list_header),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                items(
                    items = viewModel.state.current.value.collaborators,
                    key = { collaborator -> collaborator.id }
                ) { collaborator ->
                    CollaboratorListItem(
                        modifier = Modifier.fillMaxWidth(),
                        collaborator = collaborator
                    )
                }

                if (viewModel.state.current.value.collaborators.isEmpty()) {
                    if (viewModel.state.current.value.isError) {
                        item(key = "ErrorLoadingCollaboratorsItem") {
                            NoCollaboratorsCard(
                                title = stringResource(Res.string.collaborator_list_title_error),
                                description = stringResource(Res.string.collaborator_list_description_error),
                                icon = rememberVectorPainter(Icons.Default.Error),
                                error = true
                            )
                        }
                    } else {
                        item(key = "NoCollaboratorsItem") {
                            NoCollaboratorsCard(
                                title = stringResource(Res.string.collaborator_list_title_empty),
                                description = stringResource(Res.string.collaborator_list_description_empty),
                                icon = rememberVectorPainter(Icons.Default.Search),
                                error = false
                            )
                        }
                    }
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
