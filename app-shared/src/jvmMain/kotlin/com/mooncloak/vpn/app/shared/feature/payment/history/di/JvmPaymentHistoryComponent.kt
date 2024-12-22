package com.mooncloak.vpn.app.shared.feature.payment.history.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class JvmPaymentHistoryComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : PaymentHistoryComponent()

internal actual fun FeatureDependencies.Companion.createPaymentHistoryComponent(
    applicationDependencies: ApplicationComponent
): PaymentHistoryComponent = JvmPaymentHistoryComponent::class.create(
    applicationDependencies = applicationDependencies
)
