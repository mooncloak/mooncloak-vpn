package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.configure
import com.mooncloak.vpn.app.android.di.create
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.log.NoOpLogger
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Prevents screen capture and displaying contents on the "recent" screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        super.onCreate(savedInstanceState)

        setContent {
            val uriHandler = platformDefaultUriHandler()
            val applicationDependencies = ApplicationComponent.create(
                activityContext = this,
                uriHandler = uriHandler
            )

            // Disable logging if we are not in debug mode.
            if (!applicationDependencies.appClientInfo.isDebug) {
                LogPile.configure(NoOpLogger)
            }

            ApplicationRootScreen(
                component = applicationDependencies
            )
        }
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}
