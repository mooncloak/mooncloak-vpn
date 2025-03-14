package com.mooncloak.vpn.app.shared.util

import com.mikepenz.aboutlibraries.Libs
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader

public class IosLibsLoader @Inject public constructor() : LibsLoader {

    override fun load(): Libs.Builder =
        Libs.Builder() // TODO: Load the Libs
}
