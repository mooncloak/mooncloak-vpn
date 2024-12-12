package com.mooncloak.vpn.app.shared.feature.dependency.util

import com.mikepenz.aboutlibraries.Libs
import com.mooncloak.kodetools.konstruct.annotations.Inject

internal class JvmLibsLoader @Inject internal constructor() : LibsLoader {

    override fun load(): Libs.Builder =
        Libs.Builder() // TODO: Load the Libs
}
