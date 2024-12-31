package com.mooncloak.vpn.app.shared.api.network

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.util.ApplicationContext

public class AndroidLocalNetworkManager @Inject public constructor(
    private val context: ApplicationContext
) : LocalNetworkManager {

    // TODO: Implement AndroidLocalNetworkManager
    override suspend fun getInfo(): LocalNetworkInfo? = null
}
