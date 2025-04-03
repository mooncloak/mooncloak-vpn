package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun GiftCard(
    amount: String,
    timestamp: String,
    modifier: Modifier = Modifier
) {
    WalletCard(
        modifier = modifier,
        containerColor = ColorPalette.Teal_500,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = amount,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = timestamp,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = SecondaryAlpha)
        )
    }
}
