package com.mooncloak.vpn.app.shared.feature.collaborator.tip.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.feature.collaborator.tip.model.TipLinkItem
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.tip_link_item_action_tip
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TipLinkItemCard(
    item: TipLinkItem,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onSelected
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = item.icon.invoke(),
                contentDescription = item.contentDescription.invoke(),
                tint = contentColor
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = item.title.invoke(),
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = item.description.invoke(),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.copy(alpha = SecondaryAlpha),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )

            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = contentColor
                ),
                onClick = onSelected
            ) {
                Text(
                    text = stringResource(Res.string.tip_link_item_action_tip, item.price),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}
