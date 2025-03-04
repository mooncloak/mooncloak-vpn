package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.wireguard.android.backend.GoBackend

public class VPNPreparationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tunnelManager = applicationDependency { tunnelManager }

        val redirectUri = intent.getStringExtra(DeepLinkForwardingActivity.EXTRA_REDIRECT_URI)
        val prepareIntent = GoBackend.VpnService.prepare(this)

        if (prepareIntent != null) {
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                tunnelManager.finishedPreparation()

                handleRedirectAndFinish(redirectUri)
            }.launch(prepareIntent)
        } else {
            tunnelManager.finishedPreparation()

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
