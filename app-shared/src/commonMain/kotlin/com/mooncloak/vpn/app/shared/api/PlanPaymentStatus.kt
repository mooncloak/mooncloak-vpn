package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpErrorBody
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed interface PlanPaymentStatus {

    public val invoiceId: String
    public val timestamp: Instant
    public val title: String
    public val description: String?
    public val icon: String?
    public val self: String?

    @Immutable
    @Serializable
    @SerialName(value = "pending")
    public data class Pending public constructor(
        @SerialName(value = "invoice_id") override val invoiceId: String,
        @SerialName(value = "timestamp") override val timestamp: Instant,
        @SerialName(value = "title") override val title: String,
        @SerialName(value = "description") override val description: String? = null,
        @SerialName(value = "icon") override val icon: String? = null,
        @SerialName(value = "self") override val self: String? = null
    ) : PlanPaymentStatus

    @Immutable
    @Serializable
    @SerialName(value = "completed")
    public data class Completed public constructor(
        @SerialName(value = "invoice_id") override val invoiceId: String,
        @SerialName(value = "timestamp") override val timestamp: Instant,
        @SerialName(value = "title") override val title: String,
        @SerialName(value = "description") override val description: String? = null,
        @SerialName(value = "icon") override val icon: String? = null,
        @SerialName(value = "self") override val self: String? = null
    ) : PlanPaymentStatus

    @OptIn(ExperimentalApixApi::class)
    @Immutable
    @Serializable
    @SerialName(value = "error")
    public data class Error public constructor(
        @SerialName(value = "invoice_id") override val invoiceId: String,
        @SerialName(value = "timestamp") override val timestamp: Instant,
        @SerialName(value = "title") override val title: String,
        @SerialName(value = "description") override val description: String? = null,
        @SerialName(value = "icon") override val icon: String? = null,
        @SerialName(value = "self") override val self: String? = null,
        @SerialName(value = "error") public val error: HttpErrorBody<Nothing>? = null
    ) : PlanPaymentStatus
}
