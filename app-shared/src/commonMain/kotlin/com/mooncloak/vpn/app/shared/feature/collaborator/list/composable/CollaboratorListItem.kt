package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.app.Contributor
import com.mooncloak.vpn.app.shared.composable.AvatarImage

@Composable
internal fun CollaboratorListItem(
    collaborator: Contributor,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarImage(
            modifier = Modifier.size(128.dp),
            imageUri = collaborator.avatarUri,
            name = collaborator.name
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = collaborator.name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}
