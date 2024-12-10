package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable

@Immutable
public enum class ServerConnectionStatus {

    Disconnected,
    Connecting,
    Connected
}
