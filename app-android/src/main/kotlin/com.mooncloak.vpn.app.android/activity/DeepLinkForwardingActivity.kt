package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

public class DeepLinkForwardingActivity : BaseActivity() {

    private val redirectUri by lazy {
        intent?.extras?.getString(EXTRA_REDIRECT_URI)?.let { Uri.parse(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data ?: redirectUri

        // TODO: Handle deep links

        startActivity(MainActivity.newIntent(context = this))
    }

    public companion object {

        internal const val EXTRA_REDIRECT_URI = "redirectUri"

        public fun newIntent(context: Context, redirectUri: String? = null): Intent =
            Intent(context, DeepLinkForwardingActivity::class.java).apply {
                putExtra(EXTRA_REDIRECT_URI, redirectUri)
            }
    }
}
