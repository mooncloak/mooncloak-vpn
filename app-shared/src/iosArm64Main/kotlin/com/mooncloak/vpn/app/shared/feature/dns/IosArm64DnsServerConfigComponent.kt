package com.mooncloak.vpn.app.shared.feature.wireguard.dns

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createDnsServerConfigComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    listener: DnsServerOnSaveCompleteListener
): DnsServerConfigComponent = DnsServerConfigComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent,
    listener = listener
)
