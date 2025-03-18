package com.mooncloak.vpn.api.shared.support

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class FAQPage internal constructor(
    @SerialName("@id") public val idUri: String,
    @SerialName("mainEntity") public val mainEntity: List<Question> = emptyList(),
    @SerialName("name") public val name: String? = null,
    @SerialName("description") public val description: String? = null,
    @SerialName("url") public val url: String? = null,
    @SerialName("publisher") public val publisher: PersonOrOrganization? = null,
    @SerialName("datePublished") public val datePublished: Instant? = null,
    @SerialName("dateModified") public val dateModified: Instant? = null,
    @SerialName("inLanguage") public val inLanguage: String? = null,
    // Inherited from WebPage
    @SerialName("breadcrumb") public val breadcrumb: BreadcrumbList? = null,
    @SerialName("lastReviewed") public val lastReviewed: Instant? = null,
    @SerialName("relatedLink") public val relatedLink: List<String>? = null,
    // Non-standard values.
    @SerialName(value = "expiration") public val expiration: Instant? = null,
    @SerialName(value = "service_id") public val serviceId: String,
    @SerialName(value = "service_name") public val serviceName: String
) {

    @SerialName("@context")
    public val context: String = "https://schema.org"

    @SerialName("@type")
    public val type: String = "FAQPage"
}

public val FAQPage.id: String
    get() = idUri.let { uri ->
        val index = uri.lastIndexOf('/')

        if (index == -1 || index + 1 >= uri.length) {
            uri
        } else {
            uri.substring(startIndex = index + 1, endIndex = uri.length)
        }
    }

public fun FAQPage.isValid(instant: Instant = Clock.System.now()): Boolean =
    expiration == null || instant < expiration
