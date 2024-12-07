package com.mooncloak.vpn.app.android.activity

import android.os.Build
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class LauncherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // If we are Android 12+ (API 31+) we use the Splash Screen APIs. Otherwise, we use our traditional splash
        // screen which is defined by the windowBackground XML attribute in the theme style. This way we support all
        // versions appropriately.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // The splash screen documentation recommends calling installSplashScreen() before calling super.onCreate():
            // https://developer.android.com/guide/topics/ui/splash-screen/migrate
            val splashScreen = installSplashScreen()

            super.onCreate(savedInstanceState)

            // We keep the splash screen visible while this Activity is present. When we navigate to another Activity,
            // the theme style will change automatically.
            splashScreen.setKeepOnScreenCondition { true }
        } else {
            super.onCreate(savedInstanceState)
        }

        startActivity(MainActivity.newIntent(this))
        finish()
    }
}
