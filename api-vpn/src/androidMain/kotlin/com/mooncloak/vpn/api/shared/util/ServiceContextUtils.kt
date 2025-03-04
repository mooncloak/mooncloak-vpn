package com.mooncloak.vpn.api.shared.util

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService

public fun Service.launchActivity(
    intent: Intent,
    requestCode: Int = 0
) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && this is TileService -> this.launchActivityForTileService(
            intent = intent,
            requestCode = requestCode
        )

        else -> this.startActivity(intent)
    }
}

@Suppress("DEPRECATION")
@SuppressLint("StartActivityAndCollapseDeprecated")
private fun TileService.launchActivityForTileService(
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
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        startActivityAndCollapse(intent)
    } else {
        startActivity(intent)
    }
}
