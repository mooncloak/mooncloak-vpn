package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.time.Duration

/**
 * Represents a Cbor Web Message (CWM), which is like a binary Cbor version of a JSON Web Message (JWM). Where a JWM is
 * a version of a JSON Web Token (JWT), either a JWS or JWE, that is used as a message instead of a token. This model
 * is like a binary version of a JWM, that uses Cbor as the serial format instead of JSON. This allows embedding binary
 * content like images.
 *
 * Note that this is an adaptation of the Cose_Encrypt and Cose_Sign models and is designed to support both, but may be
 * slightly adapted to work with both formats.
 *
 * @see [JWM Internet Draft Specification](https://datatracker.ietf.org/doc/draft-looker-jwm/)
 * @see [Cose Specification](https://datatracker.ietf.org/doc/rfc9052/)
 */
@Serializable
public data class CborWebMessage public constructor(
    @SerialName(value = "protected") public val protected: ByteArray,
    @SerialName(value = "unprotected") public val unprotected: UnprotectedHeader? = null,
    @SerialName(value = "payload") public val payload: ByteArray? = null,
    @SerialName(value = "ciphertext") public val ciphertext: ByteArray? = null,
    @SerialName(value = "recipients") public val recipients: List<CoseRecipient>? = null,
    @SerialName(value = "signers") public val signers: List<CoseSigner>? = null,
    @SerialName(value = "iv") public val iv: ByteArray? = null,
    @SerialName(value = "tag") public val tag: ByteArray? = null,
    @SerialName(value = "aad") public val aad: ByteArray? = null
)

/**
 * A recipient defined by the Cose_Encrypt specification.
 *
 * @see [Cose Specification](https://datatracker.ietf.org/doc/rfc9052/)
 */
@Serializable
public data class CoseRecipient public constructor(
    @SerialName(value = "protected") public val protected: ByteArray,
    @SerialName(value = "unprotected") public val unprotected: UnprotectedHeader? = null,
    @SerialName(value = "ciphertext") public val ciphertext: ByteArray,
    @SerialName(value = "recipients") public val recipients: List<CoseRecipient>? = null
)

@Serializable
public data class CoseSigner public constructor(
    @SerialName(value = "protected") public val protected: ByteArray,
    @SerialName(value = "unprotected") public val unprotected: UnprotectedHeader? = null,
    @SerialName(value = "signature") public val signature: ByteArray
)

@Serializable
public data class UnprotectedHeader public constructor(
    @SerialName(value = "app_id") public val appId: String? = null
)

/**
 * Represents a header of a [CborWebMessage]. This contains fields that are present in a JWS/JWE header, as well as fields
 * relevant to the message format itself.
 *
 * @property [algorithm] The "alg" (algorithm) Header Parameter identifies the cryptographic algorithm used to secure
 * the JWS.
 *
 * @property [jwkSetUrl] The "jku" (JWK Set URL) Header Parameter is a URI
 * [RFC3986](https://datatracker.ietf.org/doc/html/rfc3986) that refers to a resource for a set of JSON-encoded public
 * keys, one of which corresponds to the key used to digitally sign the JWS.
 *
 * @property [jwk] The "jwk" (JSON Web Key) Header Parameter is the public key that corresponds to the key used to
 * digitally sign the JWS.
 *
 * @property [keyId] The "kid" (key ID) Header Parameter is a hint indicating which key was used to secure the JWS.
 *
 * @property [x5u] The "x5u" (X.509 URL) Header Parameter is a URI
 * [RFC3986](https://datatracker.ietf.org/doc/html/rfc3986) that refers to a resource for the X.509 public key
 * certificate or certificate chain [RFC5280](https://datatracker.ietf.org/doc/html/rfc5280) corresponding to the key
 * used to digitally sign the JWS.
 *
 * @property [x5c] The "x5c" (X.509 certificate chain) Header Parameter contains the X.509 public key certificate or
 * certificate chain [RFC5280](https://datatracker.ietf.org/doc/html/rfc5280) corresponding to the key used to
 * digitally sign the JWS.
 *
 * @property [x5t] The "x5t" (X.509 certificate SHA-1 thumbprint) Header Parameter is a base64url-encoded SHA-1
 * thumbprint (a.k.a. digest) of the DER encoding of the X.509 certificate
 * [RFC5280](https://datatracker.ietf.org/doc/html/rfc5280) corresponding to the key used to digitally sign the JWS.
 *
 * @property [x5tS256] The "x5t#S256" (X.509 certificate SHA-256 thumbprint) Header Parameter is a base64url-encoded
 * SHA-256 thumbprint (a.k.a. digest) of the DER encoding of the X.509 certificate
 * [RFC5280](https://datatracker.ietf.org/doc/html/rfc5280) corresponding to the key used to digitally sign the JWS.
 *
 * @property [type] The "typ" (type) Header Parameter is used by JWS applications to declare the media type
 * [IANA.MediaTypes](https://datatracker.ietf.org/doc/html/rfc7515#ref-IANA.MediaTypes) of this complete JWS.
 *
 * @property [contentType] The "cty" (content type) Header Parameter is used by JWS applications to declare the media
 * type [IANA.MediaTypes](https://datatracker.ietf.org/doc/html/rfc7515#ref-IANA.MediaTypes) of the secured content
 * (the payload).
 *
 * @property [critical] The "crit" (critical) Header Parameter indicates that extensions to this specification and/or
 * [JWA](https://datatracker.ietf.org/doc/html/rfc7515#ref-JWA) are being used that MUST be understood and processed.
 *
 * @property [compression] The "zip" (compression algorithm) Header Parameter indicates the compression algorithm
 * applied to the plaintext before encryption, if any.
 *
 * @property [encryption] The "enc" (encryption algorithm) Header Parameter identifies the content encryption algorithm
 * used to perform authenticated encryption on the plaintext to produce the ciphertext and the Authentication Tag.
 */
@Serializable
public data class ProtectedHeader public constructor(
    @SerialName(value = "alg") public val algorithm: String? = null,
    @SerialName(value = "jku") public val jwkSetUrl: String? = null,
    @SerialName(value = "jwk") public val jwk: String? = null,
    @SerialName(value = "kid") public val keyId: String? = null,
    @SerialName(value = "x5u") public val x5u: String? = null,
    @SerialName(value = "x5c") public val x5c: String? = null,
    @SerialName(value = "x5t") public val x5t: String? = null,
    @SerialName(value = "x5tS256") public val x5tS256: String? = null,
    @SerialName(value = "crit") public val critical: String? = null,
    @SerialName(value = "zip") public val compression: String? = null,
    @SerialName(value = "enc") public val encryption: String? = null,
    @SerialName(value = "typ") public val type: String? = null,
    // Should be a MIME type that contains the Payload type plus the content type. For example:
    // "application/cwm+book" where "cwm" is the Payload type, and "book" is the "@type" value of the content. This
    // value can be used with the contentContext value to retrieve the content structure, before having to decode the
    // message. This way applications can decide before hand if they support the type or not and avoid decrypting and
    // decoding if they don't support the type. However, for privacy's sake, the detail provided is optional, but at
    // the very least the "application/cwm" value should be used.
    @SerialName(value = "cty") public val contentType: String? = null,

    // Non-standard, application specific field for providing extra context about the content type.
    // @context value for the content type models (ex: "https://mooncloak.com/types").
    @SerialName(value = "ctx") public val contentContext: String? = null,

    // Non-standard, application specific field for metadata about where the message has to be sent to.
    // Note that this is different than recipients, because it can be sent to a chat room, recipients, or a larger
    // group.
    @SerialName(value = "send_to") public val sendTo: List<String>? = null
)

/**
 * Represents the claims of a [CborWebMessage]. This contains fields that are present in JWS/JWE claims, as well as fields
 * relevant to the message format itself.
 */
@Serializable
public data class Payload public constructor(
    @SerialName(value = "id") public val id: URI,
    @SerialName(value = "references") public val references: List<Reference> = emptyList(),
    @SerialName(value = "reply_to") public val replyTo: URI? = null,
    @SerialName(value = "from") public val from: DID,
    @SerialName(value = "to") public val to: List<URI> = emptyList(),
    @SerialName(value = "state_key") public val stateKey: String? = null, // EX: Matrix State Key for state events.

    // Provides more context for the message (Ex: chat room, or what the message is in reference to.)
    @SerialName(value = "context") public val context: URI? = null,

    @SerialName(value = "content") public val content: Content? = null,

    @SerialName(value = "priority") public val priority: Int? = null,

    @SerialName(value = "timestamp") public val timestamp: Instant,
    @SerialName(value = "scheduled") public val scheduled: Instant? = null,
    @SerialName(value = "expires") public val expires: Instant? = null,
    @SerialName(value = "available") public val available: Duration? = null, // EX: How long the message is available for after it is opened.
)

@JvmInline
@Serializable
public value class URI public constructor(
    public val value: String
)

@JvmInline
@Serializable
public value class DID public constructor(
    public val value: String
)
