package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.text.TextFieldStateModel
import com.mooncloak.moonscape.text.error

@Immutable
public data class RestoreCryptoStateModel public constructor(
    public val phrase: TextFieldStateModel = TextFieldStateModel(),
    public val isRestoring: Boolean = false
)

public val RestoreCryptoStateModel.isValid: Boolean
    inline get() = phrase.error == null && phrase.value.text.isNotBlank()
