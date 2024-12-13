package com.mooncloak.vpn.app.shared.feature.payment.history.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.payment.history.PaymentHistoryViewModel

@ComponentScoped
internal abstract class PaymentHistoryComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: PaymentHistoryViewModel
}

internal expect fun FeatureDependencies.Companion.createPaymentHistoryComponent(
    applicationDependencies: ApplicationComponent
): PaymentHistoryComponent
