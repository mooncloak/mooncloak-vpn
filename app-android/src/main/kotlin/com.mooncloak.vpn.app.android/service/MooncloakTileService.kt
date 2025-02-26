package com.mooncloak.vpn.app.android.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.api.shared.vpn.TunnelManager
import com.mooncloak.vpn.api.shared.vpn.isActive
import com.mooncloak.vpn.app.android.R
import com.mooncloak.vpn.app.android.activity.MainActivity
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.mooncloak.vpn.app.shared.feature.server.connection.usecase.GetDefaultServerUseCase
import com.wireguard.android.backend.GoBackend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
public class MooncloakTileService : TileService() {

    private lateinit var tunnelManager: TunnelManager
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var getDefaultServer: GetDefaultServerUseCase

    override fun onCreate() {
        super.onCreate()

        tunnelManager = this.applicationDependency { tunnelManager }
        coroutineScope = this.applicationDependency { applicationCoroutineScope }
        getDefaultServer = this.applicationDependency { getDefaultServer }
    }

    override fun onStartListening() {
        super.onStartListening()

        updateTileState(active = tunnelManager.isActive)
    }

    override fun onClick() {
        super.onClick()

        if (!isSecure) {
            unlockAndRun {
                coroutineScope.launch {
                    toggleVPN()
                }
            }
        } else {
            coroutineScope.launch {
                toggleVPN()
            }
        }
    }

    private suspend fun toggleVPN() {
        try {
            if (qsTile.state == Tile.STATE_ACTIVE) {
                stopVPN()
            } else {
                startVPN()
            }
        } catch (e: Exception) {
            LogPile.error(
                message = "Error toggling VPN connection via Quick Tile.",
                cause = e
            )

            launchMainActivity()
        }

        updateTileState(active = tunnelManager.isActive)
    }

    private suspend fun startVPN() {
        val server = getDefaultServer()

        if (server != null) {
            // We need to prepare the VPNService before connecting.
            val prepareIntent = GoBackend.VpnService.prepare(this)
            if (prepareIntent != null) {
                prepareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                launchActivity(
                    intent = prepareIntent,
                    requestCode = MooncloakVpnService.RequestCode.PREPARE
                )
            } else {
                tunnelManager.connect(server)
            }
        } else {
            launchMainActivity()
        }
    }

    private suspend fun stopVPN() {
        tunnelManager.disconnectAll()

        val vpnIntent = Intent(this, MooncloakVpnService::class.java)
        stopService(vpnIntent)
    }

    private fun launchMainActivity() {
        val mainIntent = MainActivity.newIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        launchActivity(mainIntent)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun launchActivity(
        intent: Intent,
        requestCode: Int = 0
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            startActivityAndCollapse(intent)
        }
    }

    private fun updateTileState(active: Boolean) {
        qsTile.state = if (active) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            qsTile.stateDescription = if (active) {
                getString(R.string.settings_tile_service_state_description_active)
            } else {
                getString(R.string.settings_tile_service_state_description_inactive)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            qsTile.subtitle = getString(R.string.settings_tile_service_subtitle)
        }

        qsTile.contentDescription = if (active) {
            getString(R.string.cd_settings_tile_service_action_disable)
        } else {
            getString(R.string.cd_settings_tile_service_action_enable)
        }

        qsTile.updateTile()
    }
}
