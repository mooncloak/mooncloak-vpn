package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_header
import com.mooncloak.vpn.app.shared.resource.collaborator_list_header_subtitle
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CollaboratorHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.collaborator_list_header),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(Res.string.collaborator_list_header_subtitle),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )
        )
    }
}
