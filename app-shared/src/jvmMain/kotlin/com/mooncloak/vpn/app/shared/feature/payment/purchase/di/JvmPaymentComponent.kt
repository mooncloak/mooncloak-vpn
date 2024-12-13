package com.mooncloak.vpn.app.shared.feature.payment.purchase.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.di.create

@Component
@ComponentScoped
internal abstract class JvmPaymentComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent,
    @get:Provides override val navController: NavController
) : PaymentComponent()

internal actual fun FeatureDependencies.Companion.createPaymentComponent(
    applicationDependencies: ApplicationComponent,
    navController: NavController
): PaymentComponent = JvmPaymentComponent::class.create(
    applicationDependencies = applicationDependencies,
    navController = navController
)
