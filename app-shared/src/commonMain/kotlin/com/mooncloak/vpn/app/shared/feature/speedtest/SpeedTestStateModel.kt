package com.mooncloak.vpn.app.shared.feature.speedtest

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.speedtest.SpeedTestResult
import com.mooncloak.vpn.app.shared.model.NotificationStateModel

@Immutable
public data class SpeedTestStateModel public constructor(
    public val lastResult: SpeedTestResult? = null,
    public val server: Server? = null,
    public val isLoading: Boolean = false,
    public val isTesting: Boolean = false,
    public val errorMessage: NotificationStateModel? = null,
    public val successMessage: NotificationStateModel? = null
)
