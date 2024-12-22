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
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.log.NoOpLogger
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler

public class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Prevents screen capture and displaying contents on the "recent" screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        super.onCreate(savedInstanceState)

        setContent {
            val platformUriHandler = platformDefaultUriHandler()
            val applicationDependencies = ApplicationComponent.create(
                applicationContext = this.applicationContext
            )
            val presentationDependencies = PresentationComponent.create(
                applicationComponent = applicationDependencies,
                activity = this,
                uriHandler = platformUriHandler
            )

            // Disable logging if we are not in debug mode.
            if (!applicationDependencies.appClientInfo.isDebug) {
                LogPile.configure(NoOpLogger)
            }

            ApplicationRootScreen(
                applicationComponent = applicationDependencies,
                presentationComponent = presentationDependencies,
                uriHandler = platformUriHandler
            )
        }
    }

    public companion object {

        public fun newIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }
}
