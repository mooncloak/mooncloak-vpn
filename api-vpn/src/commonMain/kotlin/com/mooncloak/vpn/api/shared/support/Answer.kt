package com.mooncloak.vpn.api.shared.support

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
public data class Answer internal constructor(
    @SerialName("@id") public val idUri: String,
    @SerialName("text") public val text: String,
    @SerialName("dateCreated") public val dateCreated: Instant? = null,
    @SerialName("author") public val author: PersonOrOrganization? = null,
    @SerialName("upvoteCount") public val upvoteCount: Int? = null,
    @SerialName("url") public val url: String? = null
) {

    @SerialName("@type")
    public val type: String = "Answer"
}

public val Answer.id: String
    get() = idUri.let { uri ->
        val index = uri.lastIndexOf('/')

        if (index == -1 || index + 1 >= uri.length) {
            uri
        } else {
            uri.substring(startIndex = index + 1, endIndex = uri.length)
        }
    }
