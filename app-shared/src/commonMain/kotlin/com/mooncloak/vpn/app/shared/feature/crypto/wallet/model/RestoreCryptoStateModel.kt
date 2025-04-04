package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.model.TextFieldStateModel

@Immutable
public data class RestoreCryptoStateModel public constructor(
    public val phrase: TextFieldStateModel = TextFieldStateModel(),
    public val isRestoring: Boolean = false
)

public val RestoreCryptoStateModel.isValid: Boolean
    inline get() = phrase.error == null && phrase.value.text.isNotBlank()
