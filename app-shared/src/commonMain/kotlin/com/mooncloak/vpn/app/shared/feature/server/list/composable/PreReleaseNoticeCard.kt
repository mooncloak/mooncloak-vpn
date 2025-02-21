package com.mooncloak.vpn.app.shared.feature.server.list.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_list_pre_release_description
import com.mooncloak.vpn.app.shared.resource.server_list_pre_release_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PreReleaseNoticeCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.server_list_pre_release_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                supportingContent = {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 8.dp),
                        text = stringResource(Res.string.server_list_pre_release_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
            )
        }
    }
}
