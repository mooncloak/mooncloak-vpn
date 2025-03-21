package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_action
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_description
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TipCard(
    onSendTip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                headlineColor = MaterialTheme.colorScheme.onBackground,
                supportingColor = MaterialTheme.colorScheme.onBackground
            ),
            headlineContent = {
                Text(
                    text = stringResource(Res.string.collaborator_list_tip_title),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            supportingContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(Res.string.collaborator_list_tip_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                    )

                    Button(
                        modifier = Modifier.sizeIn(maxWidth = 400.dp)
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = onSendTip
                    ) {
                        Text(
                            text = stringResource(Res.string.collaborator_list_tip_action)
                        )
                    }
                }
            }
        )
    }
}
