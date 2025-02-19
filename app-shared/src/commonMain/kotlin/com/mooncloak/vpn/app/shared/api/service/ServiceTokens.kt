package com.mooncloak.vpn.app.shared.api.service

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.util.DurationAsSecondsSerializer
import com.mooncloak.vpn.app.shared.api.token.Token
import com.mooncloak.vpn.app.shared.api.token.TokenType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a successful response model that can be returned from the OAuth 2.0 endpoint. These represent token
 * values obtained via the mooncloak VPN service exchange token API endpoint.
 *
 * > [!Note]
 * > That this class is modeled after the OAuth 2.0 specification, however there are notable deviations. This model may
 * > contain required properties that are not defined in the OAuth 2.0 specification, and therefore this class cannot
 * > be used generally with an OAuth 2.0 API.
 *
 * > [!Note]
 * > Attempting to access a protected resource with an invalid or expired token will result in an HTTP response of 401
 * > unauthenticated. In the event that occurs, one would need to use the [ServiceTokens.refreshToken], if available,
 * > to obtain a new [ServiceTokens], via the API refresh tokens endpoint, to access the protected resource.
 *
 * @property [id] The unique identifier for this [ServiceTokens] instance. This is typically the identifier for the
 * [ServiceTokens.accessToken] value, but no guarantees are made that they are the same. This is an extension property
 * and it is not defined by the OAuth specification.
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
 * accounts with the mooncloak VPN service. This is an extension property and it is not defined by the OAuth
 * specification.
 *
 * @see [OAuth Specification](https://www.rfc-editor.org/rfc/rfc6749.html#section-5)
 */
@OptIn(ExperimentalUuidApi::class)
@Immutable
@Serializable
public data class ServiceTokens public constructor(
    @SerialName(value = "id") public val id: String = Uuid.random().toHexString(),
    @SerialName(value = "access_token") public val accessToken: Token,
    @SerialName(value = "token_type") public val tokenType: TokenType = TokenType.Bearer,
    @SerialName(value = "expires_in") @Serializable(with = DurationAsSecondsSerializer::class) public val expiresIn: Duration,
    @SerialName(value = "refresh_token") public val refreshToken: Token? = null,
    @SerialName(value = "scope") public val scopeString: String? = null,
    @SerialName(value = "issued") public val issued: Instant,
    @SerialName(value = "expiration") public val expiration: Instant,
    @SerialName(value = "user_id") public val userId: String? = null
)

/**
 * Represents the [ServiceTokens.scopeString] value as a [List] of scope [String]s. The [ServiceTokens.scopeString]
 * value is a space separated list of scopes, whereas this is a kotlin [List] of scopes.
 */
public val ServiceTokens.scopes: List<String>
    inline get() = scopeString?.split(' ')
        ?: emptyList()

/**
 * Returns whether the [ServiceTokens.accessToken] is considered valid at the provided [instant], according to the
 * [ServiceTokens.issued] and [ServiceTokens.expiration] values.
 */
public fun ServiceTokens.isValidAt(instant: Instant): Boolean =
    instant >= issued && instant < expiration
