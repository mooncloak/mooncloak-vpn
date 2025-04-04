package com.mooncloak.vpn.app.shared.util

import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader

public class AndroidLibsLoader @Inject public constructor(
    private val activityContext: ActivityContext
) : LibsLoader {

    override fun load(): Libs.Builder =
        Libs.Builder()
            .withContext(activityContext)
}
