package com.mooncloak.vpn.app.android.service

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.mooncloak.vpn.api.shared.vpn.TunnelManager
import com.mooncloak.vpn.api.shared.vpn.isActive
import com.mooncloak.vpn.app.android.R
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.mooncloak.vpn.app.shared.feature.server.connection.usecase.GetDefaultServerUseCase
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

        coroutineScope.launch {
            if (qsTile.state == Tile.STATE_ACTIVE) {
                tunnelManager.disconnectAll()
            } else {
                val server = getDefaultServer()

                if (server != null) {
                    tunnelManager.connect(server)
                }
            }

            updateTileState(active = tunnelManager.isActive)
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
