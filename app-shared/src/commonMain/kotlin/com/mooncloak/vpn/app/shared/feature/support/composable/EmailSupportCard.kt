package com.mooncloak.vpn.app.shared.feature.support.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.support_email_action
import com.mooncloak.vpn.app.shared.resource.support_email_description
import com.mooncloak.vpn.app.shared.resource.support_email_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EmailSupportCard(
    onEmail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            SupportCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.support_email_title),
                leadingIcon = Icons.Default.Email
            )

            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(Res.string.support_email_description),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha))
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = onEmail
            ) {
                Text(text = stringResource(Res.string.support_email_action))
            }
        }
    }
}
