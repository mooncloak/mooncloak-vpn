package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.wireguard.android.backend.GoBackend
import kotlinx.coroutines.launch

/**
 * An Activity component that launches the required permissions dialog that the user must accept for us to create VPN
 * tunnel connections.
 */
public class VPNPreparationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tunnelManager = applicationDependency { tunnelManager }

        val redirectUri = intent.getStringExtra(DeepLinkForwardingActivity.EXTRA_REDIRECT_URI)
        val prepareIntent = GoBackend.VpnService.prepare(this)

        if (prepareIntent != null) {
            lifecycleScope.launch {
                this@VPNPreparationActivity.launch(prepareIntent)

                handleRedirectAndFinish(redirectUri)
            }
        } else {
            handleRedirectAndFinish(redirectUri)
        }
    }

    private fun handleRedirectAndFinish(redirectUri: String?) {
        if (redirectUri != null) {
            val intent = DeepLinkForwardingActivity.newIntent(
                context = this,
                redirectUri = redirectUri
            )

            startActivity(intent)
        }

        finish()
    }

    public companion object {

        public fun newIntent(
            context: Context,
            redirectUri: String? = null
        ): Intent = Intent(context, VPNPreparationActivity::class.java).apply {
            if (redirectUri != null) {
                putExtra(DeepLinkForwardingActivity.EXTRA_REDIRECT_URI, redirectUri)
            }
        }
    }
}
