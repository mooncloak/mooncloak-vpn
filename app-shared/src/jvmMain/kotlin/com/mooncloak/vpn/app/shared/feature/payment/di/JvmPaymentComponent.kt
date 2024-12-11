package com.mooncloak.vpn.app.shared.feature.payment.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class JvmPaymentComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : PaymentComponent()

internal actual fun FeatureDependencies.Companion.createPaymentComponent(
    applicationDependencies: ApplicationComponent
): PaymentComponent = JvmPaymentComponent::class.create(
    applicationDependencies = applicationDependencies
)
