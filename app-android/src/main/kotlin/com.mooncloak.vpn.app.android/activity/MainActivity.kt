package com.mooncloak.vpn.app.android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.app.android.api.server.AndroidVPNConnectionManager
import com.mooncloak.vpn.app.android.di.create
import com.mooncloak.vpn.app.shared.api.server.usecase.GetDefaultServerUseCase
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler

public class MainActivity : BaseActivity() {

    private var vpnConnectionManager: AndroidVPNConnectionManager? = null
    private var notificationManager: NotificationManager? = null
    private var getDefaultServer: GetDefaultServerUseCase? = null
    private var billingManager: BillingManager? = null

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
                notificationManager = applicationDependencies.notificationManager
                getDefaultServer = applicationDependencies.getDefaultServer
                billingManager = presentationDependencies.billingManager

                handleAction(intent)
            }

            ApplicationRootScreen(
                applicationComponent = applicationDependencies,
                presentationComponent = presentationDependencies,
                uriHandler = platformUriHandler
            )
        }
    }

    private suspend fun handleAction(intent: Intent?) {
        when (intent?.action) {
            ACTION_QUICK_CONNECT -> launchQuickConnect()
            ACTION_BILLING -> {
                val token = intent.getStringExtra(EXTRA_BILLING_TRANSACTION_TOKEN)?.let { TransactionToken(value = it) }
                val state = intent.getStringExtra(EXTRA_BILLING_STATE)

                if (token != null) {
                    handleBilling(
                        token = token,
                        state = state
                    )
                }
            }
        }
    }

    private suspend fun launchQuickConnect() {
        val server = getDefaultServer?.invoke()

        if (server != null) {
            vpnConnectionManager?.connect(server)
        }
    }

    private fun handleBilling(
        token: TransactionToken,
        state: String? = null
    ) {
        billingManager?.handleResult(
            token = token,
            state = state
        )
    }

    public companion object {

        private const val ACTION_QUICK_CONNECT = "com.mooncloak.vpn.app.android.action.quick_connect"
        private const val ACTION_BILLING = "com.mooncloak.vpn.app.android.action.billing"

        private const val EXTRA_BILLING_TRANSACTION_TOKEN = "com.mooncloak.vpn.app.android.extra.transaction_token"
        private const val EXTRA_BILLING_STATE = "com.mooncloak.vpn.app.android.extra.state"

        public fun newIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java)

        public fun newBillingIntent(
            context: Context,
            token: TransactionToken,
            state: String? = null
        ): Intent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_BILLING
            putExtra(EXTRA_BILLING_TRANSACTION_TOKEN, token.value)
            state?.let { putExtra(EXTRA_BILLING_STATE, it) }
        }

        public fun newQuickConnectIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java).apply {
                action = ACTION_QUICK_CONNECT
            }
    }
}
