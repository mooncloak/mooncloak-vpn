package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.vector.LunarisCoinIcon
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.home_description_lunaris_wallet
import com.mooncloak.vpn.app.shared.resource.home_title_lunaris_wallet
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LunarisWalletCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            HomeCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.home_title_lunaris_wallet),
                leadingIcon = Icons.Default.LunarisCoinIcon,
                trailingIcon = Icons.AutoMirrored.Default.ArrowForward,
                tintLeadingIcon = false
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.home_description_lunaris_wallet),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )
        }
    }
}
