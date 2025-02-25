package com.mooncloak.vpn.util.shared.platform

public actual val RuntimePlatform.Companion.current: RuntimePlatform?
    get() = RuntimePlatform.Android
