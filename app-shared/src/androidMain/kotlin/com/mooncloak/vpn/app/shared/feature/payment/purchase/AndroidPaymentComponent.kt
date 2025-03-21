package com.mooncloak.vpn.app.shared.feature.payment.purchase

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createPaymentComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent
): PaymentComponent = PaymentComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
