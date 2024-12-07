package com.mooncloak.vpn.app.desktop

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.application
import com.mooncloak.vpn.app.desktop.di.create
import com.mooncloak.vpn.app.shared.app.Application
import com.mooncloak.vpn.app.shared.di.ApplicationComponent

public fun main(): Unit = application {
    val uriHandler = object : UriHandler {
        override fun openUri(uri: String) {
            // TODO
        }
    }// FIXME: LocalUriHandler.current

    val applicationDependencies = ApplicationComponent.create(
        uriHandler = uriHandler
    )

    Application(
        component = applicationDependencies,
        builder = {
        }
    )
}
