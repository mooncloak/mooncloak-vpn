package com.mooncloak.vpn.crypto.lunaris.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EncryptedRecoveryPhrase public constructor(
    @SerialName(value = "value") public val value: Base64UrlEncodedString,
    @SerialName(value = "iv") public val iv: Base64UrlEncodedString,
    @SerialName(value = "salt") public val salt: Base64UrlEncodedString,
    @SerialName(value = "algorithm") public val algorithm: String
)
