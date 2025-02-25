package com.mooncloak.vpn.util.shared.platform

public enum class RuntimePlatform {

    Android,
    Ios,
    Linux,
    Windows,
    MacOs;

    public companion object
}

public expect val RuntimePlatform.Companion.current: RuntimePlatform?
