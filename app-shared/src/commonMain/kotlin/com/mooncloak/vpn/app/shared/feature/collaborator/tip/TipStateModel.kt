package com.mooncloak.vpn.app.shared.feature.collaborator.tip

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.vpn.app.shared.feature.collaborator.tip.model.TipLinkItem

@Immutable
public data class TipStateModel public constructor(
    public val isLoading: Boolean = true,
    public val items: List<TipLinkItem> = emptyList(),
    public val errorMessage: NotificationStateModel? = null,
    public val successMessage: NotificationStateModel? = null
)
