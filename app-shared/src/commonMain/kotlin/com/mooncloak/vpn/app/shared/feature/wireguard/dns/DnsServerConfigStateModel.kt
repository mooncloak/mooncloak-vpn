package com.mooncloak.vpn.app.shared.feature.wireguard.dns

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.model.TextFieldStateModel

@Immutable
public data class DnsServerConfigStateModel public constructor(
    public val initialPrimary: String = "",
    public val initialSecondary: String = "",
    public val primary: TextFieldStateModel = TextFieldStateModel(),
    public val secondary: TextFieldStateModel = TextFieldStateModel(),
    public val isLoading: Boolean = true,
    public val isSaving: Boolean = false,
    public val errorMessage: String? = null
)

public val DnsServerConfigStateModel.isValidated: Boolean
    get() = !isLoading
            && !isSaving
            && primary.error == null
            && secondary.error == null
            && (primary.value.text != initialPrimary || secondary.value.text != initialSecondary)
