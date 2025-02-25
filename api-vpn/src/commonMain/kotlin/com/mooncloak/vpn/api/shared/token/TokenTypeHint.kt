package com.mooncloak.vpn.api.shared.token

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a hint about the type of token submitted for revocations, defined by the
 * [OAuth 2.0 Token Revocation Specification](https://datatracker.ietf.org/doc/html/rfc7009). Per
 * the specification, for the token revocation endpoint:
 *
 * > A hint about the type of the token submitted for revocation. Clients MAY pass this parameter
 * > in order to help the authorization server to optimize the token lookup. If the server is
 * > unable to locate the token using the given hint, it MUST extend its search across all of its
 * > supported token types. An authorization server MAY ignore this parameter, particularly if it
 * > is able to detect the token type automatically. This specification defines two such values:
 * > [AccessToken] and [RefreshToken].
 * > Specific implementations, profiles, and extensions of this specification MAY define other
 * > values for this parameter using the registry defined in
 * > [Section 4.1.2](https://datatracker.ietf.org/doc/html/rfc7009#section-4.1.2).
 *
 * Note that this component can also be useful for other scenarios not specifically defined by the
 * OAuth specification, such as identifying the "type" of a token.
 *
 * @see [OAuth 2.0 Token Revocation Specification](https://datatracker.ietf.org/doc/html/rfc7009#section-2.1)
 * @see [OAuth 2.0 Specification](https://datatracker.ietf.org/doc/html/rfc6749)
 **/
@JvmInline
@Serializable
public value class TokenTypeHint public constructor(
    public val value: String
) {

    public companion object {

        /**
         * An access token as defined in
         * [RFC-6749](https://datatracker.ietf.org/doc/html/rfc6749#section-1.4).
         */
        public val AccessToken: TokenTypeHint = TokenTypeHint(value = "access_token")

        /**
         * A refresh token as defined in
         * [RFC-6749](https://datatracker.ietf.org/doc/html/rfc6749#section-1.5).
         */
        public val RefreshToken: TokenTypeHint = TokenTypeHint(value = "refresh_token")
    }
}
