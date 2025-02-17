package com.mooncloak.vpn.app.shared.feature.settings.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class SettingsAppDetails public constructor(
    @SerialName(value = "id") val id: String,
    @SerialName(value = "name") val name: String,
    @SerialName(value = "version") val version: String,
    @SerialName(value = "debug") val isDebug: Boolean,
    @SerialName(value = "pre_release") val isPreRelease: Boolean,
    @SerialName(value = "build_time") val buildTime: Instant?
)
