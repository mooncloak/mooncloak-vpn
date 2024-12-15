package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.UriHandler

public expect interface PresentationDependencies : ApplicationDependencies {

    public val uriHandler: UriHandler

    public companion object
}

@Composable
public fun <T> rememberPresentationDependency(getter: PresentationDependencies.() -> T): T {
    val dependencies = LocalPresentationComponent.current

    return remember(dependencies) { getter.invoke(dependencies) }
}
