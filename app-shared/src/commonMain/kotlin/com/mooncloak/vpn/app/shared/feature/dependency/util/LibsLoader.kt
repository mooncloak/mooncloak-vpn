package com.mooncloak.vpn.app.shared.feature.dependency.util

import com.mikepenz.aboutlibraries.Libs

public fun interface LibsLoader {

    public fun load(): Libs.Builder

    public companion object
}
