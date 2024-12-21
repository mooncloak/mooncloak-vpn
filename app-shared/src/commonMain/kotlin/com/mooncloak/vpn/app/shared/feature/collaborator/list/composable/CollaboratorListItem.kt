package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.mooncloak.vpn.app.shared.feature.collaborator.model.Collaborator

@Composable
internal fun CollaboratorListItem(
    collaborator: Collaborator,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            AsyncImage(
                model = collaborator.avatarUri,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = collaborator.name
            )
        }
    )
}
