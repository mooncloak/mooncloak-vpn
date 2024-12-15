package com.mooncloak.vpn.app.shared.feature.payment.history.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidPaymentHistoryComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : PaymentHistoryComponent()

internal actual fun FeatureDependencies.Companion.createPaymentHistoryComponent(
    presentationDependencies: PresentationComponent
): PaymentHistoryComponent = AndroidPaymentHistoryComponent::class.create(
    presentationDependencies = presentationDependencies
)
