package com.mooncloak.vpn.app.shared.info

public enum class RuntimePlatform {

    Android,
    Ios,
    Linux,
    Windows,
    MacOs;

    public companion object
}

public expect val RuntimePlatform.Companion.current: RuntimePlatform?
