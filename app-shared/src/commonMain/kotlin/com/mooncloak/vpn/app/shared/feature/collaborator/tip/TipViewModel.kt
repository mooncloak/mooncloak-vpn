package com.mooncloak.vpn.app.shared.feature.collaborator.tip

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.feature.collaborator.tip.model.TipLinks
import kotlinx.coroutines.launch

@Stable
public class TipViewModel @Inject public constructor() : ViewModel<TipStateModel>(initialStateValue = TipStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit { current ->
                current.copy(
                    items = TipLinks.all,
                    isLoading = false
                )
            }
        }
    }
}
