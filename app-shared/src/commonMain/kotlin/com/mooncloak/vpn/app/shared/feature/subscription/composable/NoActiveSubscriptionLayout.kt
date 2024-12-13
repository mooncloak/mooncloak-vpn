package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.subscription_action_protect
import com.mooncloak.vpn.app.shared.resource.subscription_description_protect
import com.mooncloak.vpn.app.shared.resource.subscription_title_no_active_plan
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NoActiveSubscriptionLayout(
    onProtect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.subscription_title_no_active_plan),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(Res.string.subscription_description_protect),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )
        )

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            onClick = onProtect
        ) {
            Text(
                text = stringResource(Res.string.subscription_action_protect)
            )
        }
    }
}
