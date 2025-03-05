package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mooncloak.vpn.app.android.service.QuickConnectService

/**
 * An activity that is launched as a result of the user selecting the "Quick Connect" app shortcut.
 *
 * This Activity component contains no UI. It simply launches the quick connect service, optionally launches the main
 * UI, then finishes.
 *
 * > [!Note]
 * > Android requires launching an Activity from app shortcuts. So this component is needed.
 */
public class QuickConnectActivity public constructor() : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        QuickConnectService.launchFrom(this)

        startActivity(MainActivity.newIntent(this))

        finish()
    }

    public companion object {

        private const val ACTION = "com.mooncloak.vpn.app.android.service.action.quick_connect"

        public fun newIntent(context: Context): Intent = Intent(context, QuickConnectActivity::class.java).apply {
            action = ACTION
        }
    }
}
