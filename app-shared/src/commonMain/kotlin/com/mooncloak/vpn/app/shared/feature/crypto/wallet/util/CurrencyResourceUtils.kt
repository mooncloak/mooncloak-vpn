package com.mooncloak.vpn.app.shared.feature.crypto.wallet.util

import androidx.compose.runtime.Composable
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_token_protocol_erc20
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Lunaris
import org.jetbrains.compose.resources.stringResource

public val Currency.protocol: String?
    @Composable
    get() = when (this) {
        Currency.Lunaris -> stringResource(Res.string.crypto_wallet_value_token_protocol_erc20)
        else -> null
    }
