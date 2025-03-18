package com.mooncloak.vpn.api.shared.support

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
public data class Question public constructor(
    @SerialName("@id") public val idUri: String,
    @SerialName("name") public val name: String,
    @SerialName("acceptedAnswer") public val acceptedAnswer: Answer,
    @SerialName("suggestedAnswer") public val suggestedAnswer: List<Answer>? = null,
    @SerialName("answerCount") public val answerCount: Int? = null,
    @SerialName("dateCreated") public val dateCreated: Instant? = null,
    @SerialName("author") public val author: PersonOrOrganization? = null
) {

    @SerialName("@type")
    public val type: String = "Question"
}

public val Question.id: String
    get() = idUri.let { uri ->
        val index = uri.lastIndexOf('/')

        if (index == -1 || index + 1 >= uri.length) {
            uri
        } else {
            uri.substring(startIndex = index + 1, endIndex = uri.length)
        }
    }
