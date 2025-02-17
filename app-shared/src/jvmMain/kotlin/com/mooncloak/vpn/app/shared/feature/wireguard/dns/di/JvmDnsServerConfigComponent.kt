package com.mooncloak.vpn.app.shared.feature.wireguard.dns.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerOnSaveCompleteListener

@Component
@FeatureScoped
internal abstract class JvmDnsServerConfigComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
    @get:Provides override val listener: DnsServerOnSaveCompleteListener
) : DnsServerConfigComponent()

internal actual fun FeatureDependencies.Companion.createDnsServerConfigComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    listener: DnsServerOnSaveCompleteListener
): DnsServerConfigComponent = JvmDnsServerConfigComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent,
    listener = listener
)
