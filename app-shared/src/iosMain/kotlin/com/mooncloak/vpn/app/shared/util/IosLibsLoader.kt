package com.mooncloak.vpn.app.shared.util

import com.mikepenz.aboutlibraries.Libs
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

public class IosLibsLoader @Inject public constructor() : LibsLoader {

    override fun load(): Libs.Builder {
        // Get the main bundle
        val bundle = NSBundle.mainBundle

        // Find the resource path for aboutlibraries.json
        val filePath = bundle.pathForResource("aboutlibraries", ofType = "json")
            ?: return Libs.Builder() // Return empty builder if file not found

        // Load the file contents as NSData
        val data = NSData.dataWithContentsOfFile(filePath)
            ?: return Libs.Builder() // Return empty builder if loading fails

        val bytes = data.toByteArray()
        val jsonString = bytes.decodeToString()

        return Libs.Builder().withJson(jsonString)
    }
}
