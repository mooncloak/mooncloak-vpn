package com.mooncloak.vpn.app.shared.api.service

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.util.DurationAsSecondsSerializer
import com.mooncloak.vpn.app.shared.api.token.Token
import com.mooncloak.vpn.app.shared.api.token.TokenType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.time.Duration

/**
 * Represents a successful response model that can be returned from the OAuth 2.0 endpoint.
 *
 * @property [accessToken] REQUIRED. The access token issued by the authorization server.
 *
 * @property [tokenType] REQUIRED. The type of the token issued as described in
 * [Section 7.1](https://www.rfc-editor.org/rfc/rfc6749.html#section-7.1). Value is case
 * insensitive.
 *
 * @property [expiresIn] RECOMMENDED. The lifetime in seconds of the access token. For example, the
 * value "3600" denotes that the access token will expire in one hour from the time the response
 * was generated. If omitted, the authorization server SHOULD provide the expiration time via other
 * means or document the default value.
 *
 * @property [refreshToken] OPTIONAL. The refresh token, which can be used to obtain new access
 * tokens using the same authorization grant as described in
 * [Section 6](https://www.rfc-editor.org/rfc/rfc6749.html#section-6).
 *
 * @property [scopeString] OPTIONAL, if identical to the scope requested by the client; otherwise,
 * REQUIRED. The scope of the access token as described by Section 3.3.
 *
 * @property [issued] Represents when the token values were issued. This is an extension property
 * and it is not defined by the OAuth specification.
 *
 * @property [expiration] Represents when the token values expire. This is an extension property
 * and it is not defined by the OAuth specification.
 *
 * @property [userId] A unique identifier value used to identify the user associated with these [ServiceTokens]. This
 * value is typically automatically generated via the service during the token exchange process since there are no user
 * accounts with the mooncloak VPN service.
 *
 * @see [OAuth Specification](https://www.rfc-editor.org/rfc/rfc6749.html#section-5)
 */
@Immutable
@Serializable
public data class ServiceTokens public constructor(
    @SerialName(value = "access_token") public val accessToken: Token,
    @SerialName(value = "token_type") public val tokenType: TokenType = TokenType.Bearer,
    @SerialName(value = "expires_in") @Serializable(with = DurationAsSecondsSerializer::class) public val expiresIn: Duration,
    @SerialName(value = "refresh_token") public val refreshToken: Token? = null,
    @SerialName(value = "scope") public val scopeString: String? = null,
    @SerialName(value = "issued") public val issued: Instant? = null,
    @SerialName(value = "expiration") public val expiration: Instant? = null,
    @SerialName(value = "user_id") public val userId: String? = null
)

public val ServiceTokens.scopes: List<String>
    inline get() = scopeString?.split(' ')
        ?: emptyList()
