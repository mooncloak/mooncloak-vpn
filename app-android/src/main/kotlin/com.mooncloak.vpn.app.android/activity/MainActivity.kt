package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import com.mooncloak.vpn.app.android.di.create
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.di.PresentationComponent
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
            val coroutineScope = rememberCoroutineScope()
            val platformUriHandler = platformDefaultUriHandler()
            val applicationDependencies = this.applicationComponent
            val presentationDependencies = PresentationComponent.create(
                applicationComponent = applicationDependencies,
                coroutineScope = coroutineScope,
                activity = this,
                uriHandler = platformUriHandler
            )

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
