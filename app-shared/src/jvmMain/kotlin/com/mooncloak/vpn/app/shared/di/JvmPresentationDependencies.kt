package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler

public actual interface PresentationDependencies : ApplicationDependencies {

    public actual val uriHandler: UriHandler

    public actual companion object
}
