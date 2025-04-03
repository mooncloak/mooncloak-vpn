package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_action
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_description
import com.mooncloak.vpn.app.shared.resource.collaborator_list_tip_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TipTeamCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            HomeCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.collaborator_list_tip_title),
                leadingIcon = Icons.Default.AttachMoney
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.collaborator_list_tip_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = onClick
            ) {
                Text(
                    text = stringResource(Res.string.collaborator_list_tip_action)
                )
            }
        }
    }
}
