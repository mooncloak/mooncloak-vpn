package com.mooncloak.vpn.app.shared.feature.collaborator.tip.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
public data class TipLinkItem public constructor(
    public val url: String,
    public val price: String,
    public val title: @Composable () -> String,
    public val description: @Composable () -> String,
    public val icon: @Composable () -> Painter,
    public val contentDescription: @Composable () -> String? = { null }
)
