package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents an arbitrary token categorization identifier value. This is typically an optional
 * field that provides additional information about a token. Some protocols require specifying this
 * value so that a client can know how to use the token in requests.
 */
@Immutable
@Serializable
@JvmInline
public value class TokenType public constructor(
    public val value: String
) {

    public companion object {

        /**
         * Represents a bearer token that is included with the authorization header for resource
         * API requests.
         */
        public val Bearer: TokenType = TokenType(value = "Bearer")

        /**
         * Represents a DPoP (demonstrating proof of possession) token type value as per
         * specification RFC-9449: https://datatracker.ietf.org/doc/html/rfc9449
         */
        public val DPoP: TokenType = TokenType(value = "DPoP")
    }
}
