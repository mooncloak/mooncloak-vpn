package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.app.Contributor

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CollaboratorListItem(
    collaborator: Contributor,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        AvatarImage(
            modifier = Modifier.size(64.dp)
                .align(Alignment.CenterVertically),
            imageUri = collaborator.avatarUri,
            name = collaborator.name
        )

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = collaborator.name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}
