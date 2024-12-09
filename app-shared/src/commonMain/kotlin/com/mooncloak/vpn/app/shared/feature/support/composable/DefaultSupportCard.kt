package com.mooncloak.vpn.app.shared.feature.support.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun DefaultSupportCard(
    title: String,
    description: String,
    action: String,
    icon: ImageVector?,
    onAction: () -> Unit,
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
                leadingText = title,
                leadingIcon = icon
            )

            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha))
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = onAction
            ) {
                Text(text = action)
            }
        }
    }
}
