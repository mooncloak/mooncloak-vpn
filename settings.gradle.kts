pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://repo.repsy.io/mvn/mooncloak/public")
    }

    dependencyResolutionManagement {
        @Suppress("UnstableApiUsage")
        repositories {
            mavenCentral()
            google()
            maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
            maven("https://repo.repsy.io/mvn/mooncloak/public")
            maven("https://jitpack.io")
        }
    }

    includeBuild("build-logic")
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.5"

    // See build.gradle.kts file in root project folder for the rest of the plugins applied.
}

rootProject.name = "mooncloak-vpn"

include(":util-shared")
include(":util-shortcuts")
include(":util-notification")
include(":util-permission")

include(":data-shared")
include(":data-sqlite")

include(":api-vpn")

include(":network-core")

include(":app-shared")
include(":app-android")
include(":app-desktop")
