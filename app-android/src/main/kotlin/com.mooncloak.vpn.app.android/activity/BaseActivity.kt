package com.mooncloak.vpn.app.android.activity

import androidx.fragment.app.FragmentActivity
import com.mooncloak.vpn.app.android.MooncloakVpnApplication
import com.mooncloak.vpn.app.shared.di.ApplicationComponent

public abstract class BaseActivity : FragmentActivity() {

    protected val applicationComponent: ApplicationComponent
        get() = (this.applicationContext as MooncloakVpnApplication).applicationComponent
}
