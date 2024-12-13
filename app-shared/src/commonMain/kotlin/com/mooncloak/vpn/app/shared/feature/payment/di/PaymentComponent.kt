package com.mooncloak.vpn.app.shared.feature.payment.di

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.PaymentViewModel

@ComponentScoped
internal abstract class PaymentComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: PaymentViewModel

    abstract val navController: NavController
}

internal expect fun FeatureDependencies.Companion.createPaymentComponent(
    applicationDependencies: ApplicationComponent,
    navController: NavController
): PaymentComponent
