package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import kotlinx.coroutines.launch

@Stable
@FeatureScoped
public class SupportViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo
) : ViewModel<SupportStateModel>(initialStateValue = SupportStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit(
                value = state.current.value.copy(
                    supportEmailAddress = appClientInfo.supportEmail,
                    featureRequestUri = appClientInfo.supportFeatureRequestUri,
                    issueRequestUri = appClientInfo.supportIssueUri,
                    rateAppUri = appClientInfo.rateAppUri
                )
            )
        }
    }
}
