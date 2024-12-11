package com.mooncloak.vpn.app.shared.feature.dependency

import androidx.compose.runtime.Immutable
import com.mikepenz.aboutlibraries.Libs

@Immutable
public data class DependencyLicenseListStateModel public constructor(
    public val libs: Libs? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
