package com.mooncloak.vpn.app.shared.feature.collaborator.container

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class CollaboratorContainerViewModel @Inject public constructor(

) : ViewModel<CollaboratorContainerStateModel>(initialStateValue = CollaboratorContainerStateModel()) {

    public fun load() {
        // TODO:
    }
}
