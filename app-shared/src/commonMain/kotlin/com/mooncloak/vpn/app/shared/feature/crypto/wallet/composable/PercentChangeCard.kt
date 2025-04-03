package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.util.PercentageFormatter
import com.mooncloak.vpn.app.shared.util.format
import com.mooncloak.vpn.app.shared.util.invoke
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PercentChangeCard(
    label: String,
    value: Number?,
    modifier: Modifier = Modifier,
    formatter: PercentageFormatter = remember { PercentageFormatter() }
) {
    val formatted = remember(value, formatter) { value?.let { formatter.format(it) } }
    val localContentColor = LocalContentColor.current
    val color = remember(value, localContentColor) {
        val double = value?.toDouble()

        when {
            double == null -> localContentColor
            double > 0.1 -> ColorPalette.Teal_500
            double < 0.0 -> ColorPalette.MooncloakError
            else -> localContentColor
        }
    }
    val icon = remember(value) {
        val double = value?.toDouble()

        when {
            double == null -> null
            double > 0.1 -> Icons.Default.ArrowUpward
            double < 0.0 -> Icons.Default.ArrowDownward
            else -> null
        }
    }

    WalletCard(
        modifier = modifier
    ) {
        LabeledContent(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            label = label,
            value = formatted ?: stringResource(Res.string.global_not_available),
            valueColor = color,
            icon = icon
        )
    }
}
