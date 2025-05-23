package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createCryptoWalletComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): CryptoWalletComponent = CryptoWalletComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
