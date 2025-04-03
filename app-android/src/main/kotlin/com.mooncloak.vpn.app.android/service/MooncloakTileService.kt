package com.mooncloak.vpn.app.android.service

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.network.core.tunnel.isConnected
import com.mooncloak.vpn.app.android.util.launchActivity
import com.mooncloak.vpn.app.android.R
import com.mooncloak.vpn.app.android.activity.MainActivity
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.mooncloak.vpn.app.shared.api.server.usecase.GetDefaultServerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@RequiresApi(Build.VERSION_CODES.N)
public class MooncloakTileService : TileService() {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var tunnelManager: TunnelManager
    private lateinit var getDefaultServer: GetDefaultServerUseCase

    private val mutex = Mutex(locked = false)

    override fun onCreate() {
        super.onCreate()

        coroutineScope = this.applicationDependency { applicationCoroutineScope }
        tunnelManager = this.applicationDependency { tunnelManager }
        getDefaultServer = this.applicationDependency { getDefaultServer }
    }

    override fun onStartListening() {
        super.onStartListening()

        updateTileState(active = tunnelManager.isConnected)
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
        mutex.withLock {
            try {
                if (qsTile.state == Tile.STATE_ACTIVE) {
                    // Update the state first, as there is no loading animation in the quick settings tile UI, so we
                    // don't want the user to get the impression that it didn't work.
                    updateTileState(active = false)

                    stopVPN()
                } else {
                    // Update the state first, as there is no loading animation in the quick settings tile UI, so we
                    // don't want the user to get the impression that it didn't work.
                    updateTileState(active = true)

                    startVPN()
                }
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error toggling VPN connection via Quick Tile.",
                    cause = e
                )

                launchMainActivity()
            }

            updateTileState(active = tunnelManager.isConnected)
        }
    }

    private suspend fun startVPN() {
        val server = getDefaultServer()

        if (server != null) {
            val prepareIntent = tunnelManager.prepare(context = this)

            if (prepareIntent == null) {
                tunnelManager.connect(server)
            } else {
                launchActivity(prepareIntent)
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
