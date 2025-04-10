package com.mooncloak.vpn.app.shared.util

import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withJson
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader

public class JvmLibsLoader @Inject public constructor() : LibsLoader {

    override fun load(): Libs.Builder {
        val bytes = this::class.java.classLoader?.getResourceAsStream("aboutlibraries.json")?.readAllBytes()

        var builder = Libs.Builder()

        if (bytes != null) {
            builder = builder.withJson(bytes)
        }

        return builder
    }
}
