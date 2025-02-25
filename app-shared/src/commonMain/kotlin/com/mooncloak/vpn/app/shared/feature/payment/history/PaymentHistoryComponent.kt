package com.mooncloak.vpn.app.shared.feature.payment.history

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class PaymentHistoryComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : FeatureDependencies {

    abstract override val viewModel: PaymentHistoryViewModel
}

internal expect fun FeatureDependencies.Companion.createPaymentHistoryComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): PaymentHistoryComponent
