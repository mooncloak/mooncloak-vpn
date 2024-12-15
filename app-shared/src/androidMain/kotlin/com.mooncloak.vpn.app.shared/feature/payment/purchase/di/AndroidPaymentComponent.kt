package com.mooncloak.vpn.app.shared.feature.payment.purchase.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidPaymentComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent,
    @get:Provides override val navController: NavController
) : PaymentComponent()

internal actual fun FeatureDependencies.Companion.createPaymentComponent(
    presentationDependencies: PresentationComponent,
    navController: NavController
): PaymentComponent = AndroidPaymentComponent::class.create(
    presentationDependencies = presentationDependencies,
    navController = navController
)
