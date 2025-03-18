package com.mooncloak.vpn.api.shared.support


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
public data class BreadcrumbList public constructor(
    @SerialName("itemListElement") public val itemListElement: List<BreadcrumbItem>? = null
) {

    @SerialName("@type")
    public val type: String = "BreadcrumbList"
}

@Serializable
public data class BreadcrumbItem public constructor(
    @SerialName("position") public val position: Int,
    @SerialName("name") public val name: String,
    @SerialName("item") public val item: String? = null
) {

    @SerialName("@type")
    public val type: String = "ListItem"
}
