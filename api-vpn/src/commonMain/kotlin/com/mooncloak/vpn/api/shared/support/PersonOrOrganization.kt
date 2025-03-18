package com.mooncloak.vpn.api.shared.support

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
public data class PersonOrOrganization public constructor(
    @SerialName(value = Key.TYPE) public val type: String,
    @SerialName(value = Key.ID) public val idUri: String,
    @SerialName(value = Key.NAME) public val name: String? = null,
    @SerialName(value = Key.URL) public val url: String? = null
) {

    public object Key {

        public const val TYPE: String = "@type"
        public const val ID: String = "@id"
        public const val NAME: String = "name"
        public const val URL: String = "url"
    }

    public object Discriminator {

        public const val PERSON: String = "Person"
        public const val ORGANIZATION: String = "Organization"
    }
}

public val PersonOrOrganization.id: String
    get() = idUri.let { uri ->
        val index = uri.lastIndexOf('/')

        if (index == -1 || index + 1 >= uri.length) {
            uri
        } else {
            uri.substring(startIndex = index + 1, endIndex = uri.length)
        }
    }
