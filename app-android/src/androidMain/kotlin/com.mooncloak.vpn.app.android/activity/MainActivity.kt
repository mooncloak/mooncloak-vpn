package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import com.mooncloak.vpn.app.android.di.create
import com.mooncloak.vpn.app.shared.app.Application
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
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

            Application(
                component = applicationDependencies,
                builder = {
                }
            )
        }
    }

    companion object {

        fun newIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }
}
