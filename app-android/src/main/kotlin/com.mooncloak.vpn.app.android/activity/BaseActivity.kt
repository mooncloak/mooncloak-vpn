package com.mooncloak.vpn.app.android.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.mooncloak.vpn.app.android.MooncloakVpnApplication
import com.mooncloak.vpn.app.shared.di.ApplicationComponent

public abstract class BaseActivity : FragmentActivity() {

    protected val applicationComponent: ApplicationComponent
        get() = (this.applicationContext as MooncloakVpnApplication).applicationComponent

    protected open val isEdgeToEdgeEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isEdgeToEdgeEnabled) {
            // According to the documentation, this should be enabled here to set up edge-to-edge with the default style.
            enableEdgeToEdge()
        }

        super.onCreate(savedInstanceState)
    }
}
