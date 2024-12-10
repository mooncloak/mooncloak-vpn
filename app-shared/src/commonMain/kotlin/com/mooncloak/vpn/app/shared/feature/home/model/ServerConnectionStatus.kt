package com.mooncloak.vpn.app.shared.feature.home.model

import androidx.compose.runtime.Immutable

@Immutable
public enum class ServerConnectionStatus {

    Disconnected,
    Connecting,
    Connected
}
