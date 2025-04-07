package com.mooncloak.vpn.crypto.lunaris.model

import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
public value class Base64UrlEncodedString public constructor(
    public val value: String
)

@OptIn(ExperimentalEncodingApi::class)
public fun ByteArray.encodeToBase64UrlString(): Base64UrlEncodedString =
    Base64UrlEncodedString(value = Base64.UrlSafe.encode(this))

@OptIn(ExperimentalEncodingApi::class)
public fun Base64UrlEncodedString.decode(): ByteArray =
    Base64.UrlSafe.decode(this.value)
