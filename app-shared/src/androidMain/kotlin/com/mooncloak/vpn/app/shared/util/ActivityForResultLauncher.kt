package com.mooncloak.vpn.app.shared.util

import android.content.Intent
import androidx.activity.result.ActivityResult

public interface ActivityForResultLauncher {

    public suspend fun launch(
        intent: Intent
    ): ActivityResult

    public companion object
}
