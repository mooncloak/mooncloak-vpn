package com.mooncloak.vpn.app.shared.feature.payment.purchase.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class AndroidPaymentComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent,
    @get:Provides override val navController: NavController
) : PaymentComponent()

internal actual fun FeatureDependencies.Companion.createPaymentComponent(
    applicationDependencies: ApplicationComponent,
    navController: NavController
): PaymentComponent = AndroidPaymentComponent::class.create(
    applicationDependencies = applicationDependencies,
    navController = navController
)
