package com.mooncloak.vpn.app.shared.feature.payment.history.di

import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.payment.history.PaymentHistoryViewModel

@FeatureScoped
internal abstract class PaymentHistoryComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: PaymentHistoryViewModel
}

internal expect fun FeatureDependencies.Companion.createPaymentHistoryComponent(
    presentationDependencies: PresentationComponent
): PaymentHistoryComponent
