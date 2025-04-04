package com.mooncloak.vpn.app.android

import android.app.Application
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.configure
import com.mooncloak.vpn.app.android.di.AndroidApplicationComponent
import com.mooncloak.vpn.app.android.di.create
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.util.log.NoOpLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

public class MooncloakVpnApplication : Application() {

    public lateinit var applicationComponent: AndroidApplicationComponent

    public val coroutineScope: CoroutineScope = MainScope()

    override fun onCreate() {
        super.onCreate()

        applicationComponent = ApplicationComponent.create(
            applicationContext = this.applicationContext,
            coroutineScope = coroutineScope
        )

        // Disable logging if we are not in debug mode.
        if (!applicationComponent.appClientInfo.isDebug) {
            LogPile.configure(NoOpLogger)
        }

        // The web3j cryptocurrency library requires BouncyCastle to be installed for its operations. If its missing,
        // we cannot create or restore wallets.
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
    }
}
