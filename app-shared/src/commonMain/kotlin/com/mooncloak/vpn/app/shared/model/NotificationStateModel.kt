package com.mooncloak.vpn.app.shared.model

import androidx.compose.runtime.Immutable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Immutable
public data class NotificationStateModel public constructor(
    public val message: String,
    public val id: String = Uuid.random().toHexString()
)
