package com.mooncloak.vpn.data.shared.util

import java.nio.file.Paths

public fun getDatabaseFileLocation(appName: String): String {
    val userHome = System.getProperty("user.home")
    val os = System.getProperty("os.name").lowercase()

    val baseDir = when {
        os.contains("windows") || os.contains("ming") -> Paths.get(userHome, "AppData", "Local", appName)
        os.contains("mac") || os.contains("osx") -> Paths.get(userHome, "Library", "Application Support", appName)
        else -> Paths.get(userHome, ".local", "share", appName)
    }

    // Ensure the directory exists
    val directory = baseDir.toFile()
    if (!directory.exists()) {
        directory.mkdirs()
    }

    return Paths.get(baseDir.toString(), "mooncloak_vpn.db").toString()
}
