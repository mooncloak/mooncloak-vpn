package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class CollaboratorListViewModel @Inject public constructor(

) : ViewModel<CollaboratorListStateModel>(initialStateValue = CollaboratorListStateModel()) {

    public fun load() {
        // TODO:
    }
}
