package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.mooncloak.vpn.app.android.api.server.AndroidVPNConnectionManager
import com.mooncloak.vpn.app.android.di.create
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.util.notification.AndroidNotificationManager
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler

public class MainActivity : BaseActivity() {

    private var vpnConnectionManager: AndroidVPNConnectionManager? = null
    private var notificationManager: AndroidNotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Prevents screen capture and displaying contents on the "recent" screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        super.onCreate(savedInstanceState)

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val platformUriHandler = platformDefaultUriHandler()
            val applicationDependencies = this.applicationComponent
            val presentationDependencies = PresentationComponent.create(
                applicationComponent = applicationDependencies,
                coroutineScope = coroutineScope,
                activity = this,
                uriHandler = platformUriHandler
            )

            LaunchedEffect(presentationDependencies) {
                // FIXME: Casts. I don't like having to cast here. Probably can update the Android
                //  PresentationDependencies to use the Android type instead of the common type for each property here.
                //  Will have to check if that works with expect/actual or find another approach.
                vpnConnectionManager = presentationDependencies.vpnConnectionManager as? AndroidVPNConnectionManager
                notificationManager = presentationDependencies.notificationManager as? AndroidNotificationManager
            }

            ApplicationRootScreen(
                applicationComponent = applicationDependencies,
                presentationComponent = presentationDependencies,
                uriHandler = platformUriHandler
            )
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        vpnConnectionManager?.receivedResult(
            requestCode = requestCode,
            resultCode = resultCode,
            data = data
        )
        notificationManager?.receivedResult(
            requestCode = requestCode,
            resultCode = resultCode
        )
    }

    public companion object {

        public fun newIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }
}
