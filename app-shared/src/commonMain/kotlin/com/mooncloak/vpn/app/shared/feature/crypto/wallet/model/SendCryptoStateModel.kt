package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.text.TextFieldStateModel
import com.mooncloak.moonscape.text.error
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAddress

@Immutable
public data class SendCryptoStateModel public constructor(
    public val address: TextFieldStateModel = TextFieldStateModel(),
    public val suggestedRecipients: List<CryptoAddress> = emptyList(),
    public val amount: TextFieldStateModel = TextFieldStateModel(),
    public val estimatedGas: String? = null,
    public val isSending: Boolean = false
)

public val SendCryptoStateModel.isValid: Boolean
    get() = address.value.text.isNotBlank()
            && address.error == null
            && amount.value.text.isNotBlank()
            && amount.error == null
