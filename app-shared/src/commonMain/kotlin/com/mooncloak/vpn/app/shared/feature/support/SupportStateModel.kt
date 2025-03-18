package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.support.FAQPage

@Immutable
public data class SupportStateModel public constructor(
    public val supportEmailAddress: String? = null,
    public val rateAppUri: String? = null,
    public val featureRequestUri: String? = null,
    public val issueRequestUri: String? = null,
    public val faqPages: List<FAQPage> = emptyList(),
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
