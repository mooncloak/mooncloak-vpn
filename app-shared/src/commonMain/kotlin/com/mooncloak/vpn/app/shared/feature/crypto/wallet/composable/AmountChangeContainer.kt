package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_all_time
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_today
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AmountChangeContainer(
    today: Number,
    allTime: Number,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PercentChangeCard(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.crypto_wallet_label_today),
            value = today
        )

        PercentChangeCard(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.crypto_wallet_label_all_time),
            value = allTime
        )
    }
}
