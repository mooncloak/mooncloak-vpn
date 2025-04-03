package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.util.ActivityForResultLauncher
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.network.core.vpn.BaseVPNConnectionManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.datetime.Clock

internal class AndroidVPNConnectionManager @Inject internal constructor(
    coroutineScope: ApplicationCoroutineScope,
    serverConnectionRecordRepository: ServerConnectionRecordRepository,
    clock: Clock,
    private val tunnelManager: TunnelManager,
    private val activity: Activity,
    private val activityForResultLauncher: ActivityForResultLauncher
) : BaseVPNConnectionManager(
    coroutineScope = coroutineScope,
    serverConnectionRecordRepository = serverConnectionRecordRepository,
    clock = clock,
    tunnelManager = tunnelManager
) {

    override suspend fun prepare(): Boolean {
        val prepareIntent = tunnelManager.prepare(context = activity)

        if (prepareIntent != null) {
            // TODO: MooncloakVpnService.RequestCode.PREPARE

            val activityResult = activityForResultLauncher.launch(prepareIntent)

            return activityResult.resultCode == Activity.RESULT_OK
        }

        return true
    }
}
