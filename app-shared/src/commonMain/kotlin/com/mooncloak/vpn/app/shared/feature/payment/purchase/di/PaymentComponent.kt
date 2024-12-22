package com.mooncloak.vpn.app.shared.feature.payment.purchase.di

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentViewModel

@FeatureScoped
internal abstract class PaymentComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: PaymentViewModel

    abstract val navController: NavController
}

internal expect fun FeatureDependencies.Companion.createPaymentComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): PaymentComponent
