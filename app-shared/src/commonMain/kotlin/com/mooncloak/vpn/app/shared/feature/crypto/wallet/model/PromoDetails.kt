package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
public data class PromoDetails public constructor(
    public val title: String,
    public val description: String? = null,
    public val icon: @Composable () -> Painter
)
