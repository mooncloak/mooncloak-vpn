package com.mooncloak.vpn.app.shared.info

public actual val RuntimePlatform.Companion.current: RuntimePlatform?
    get() {
        val osName = System.getProperty("os.name")?.lowercase()

        return when {
            osName == null -> null
            osName.contains("mac") -> RuntimePlatform.MacOs
            osName.contains("darwin") -> RuntimePlatform.MacOs
            osName.contains("linux") -> RuntimePlatform.Linux
            osName.contains("windows") -> RuntimePlatform.Windows
            osName.contains("mingw") -> RuntimePlatform.Windows
            else -> null
        }
    }
