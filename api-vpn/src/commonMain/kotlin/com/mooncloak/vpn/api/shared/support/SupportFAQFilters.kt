package com.mooncloak.vpn.api.shared.support

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SupportFAQFilters public constructor(
    @SerialName(value = Key.SERVICE_NAME) public val serviceName: String? = null,
    @SerialName(value = Key.SERVICE_ID) public val serviceId: String? = null,
    @SerialName(value = Key.CATEGORY) public val category: String? = null,
    @SerialName(value = Key.LANGUAGE) public val language: String? = null
) {

    public object Key {

        public const val SERVICE_NAME: String = "service_name"
        public const val SERVICE_ID: String = "service_id"
        public const val CATEGORY: String = "category"
        public const val LANGUAGE: String = "language"
    }

    public object QueryParam {

        public const val SERVICE_NAME: String = "filters.${Key.SERVICE_NAME}"
        public const val SERVICE_ID: String = "filters.${Key.SERVICE_ID}"
        public const val CATEGORY: String = "filters.${Key.CATEGORY}"
        public const val LANGUAGE: String = "filters.${Key.LANGUAGE}"
    }
}
