package com.mooncloak.vpn.app.shared.feature.payment.history

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createPaymentHistoryComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): PaymentHistoryComponent = PaymentHistoryComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
