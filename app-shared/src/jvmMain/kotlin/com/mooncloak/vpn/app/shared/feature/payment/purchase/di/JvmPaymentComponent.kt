package com.mooncloak.vpn.app.shared.feature.payment.purchase.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmPaymentComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent
) : PaymentComponent()

internal actual fun FeatureDependencies.Companion.createPaymentComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent
): PaymentComponent = JvmPaymentComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
