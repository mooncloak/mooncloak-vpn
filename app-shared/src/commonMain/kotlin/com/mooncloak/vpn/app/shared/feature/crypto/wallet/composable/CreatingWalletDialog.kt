package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mooncloak.vpn.app.shared.composable.MooncloakDialog
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_dialog_creating
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_dialog_creating
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CreatingWalletDialog(
    modifier: Modifier = Modifier
) {
    MooncloakDialog(
        modifier = modifier,
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator()

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(Res.string.crypto_wallet_title_dialog_creating),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.crypto_wallet_description_dialog_creating),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
        )
    }
}
