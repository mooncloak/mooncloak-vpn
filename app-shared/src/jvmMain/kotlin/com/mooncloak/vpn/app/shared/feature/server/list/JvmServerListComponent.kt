package com.mooncloak.vpn.app.shared.feature.server.list

import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createServerListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    serverDetailsBottomSheetState: ManagedModalBottomSheetState
): ServerListComponent = ServerListComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent,
    serverDetailsBottomSheetState = serverDetailsBottomSheetState
)
