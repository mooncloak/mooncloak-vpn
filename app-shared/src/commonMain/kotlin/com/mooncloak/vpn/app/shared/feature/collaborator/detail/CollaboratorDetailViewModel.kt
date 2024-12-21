package com.mooncloak.vpn.app.shared.feature.collaborator.detail

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class CollaboratorDetailViewModel @Inject public constructor(

) : ViewModel<CollaboratorDetailStateModel>(initialStateValue = CollaboratorDetailStateModel()) {

    public fun load() {
        // TODO:
    }
}
