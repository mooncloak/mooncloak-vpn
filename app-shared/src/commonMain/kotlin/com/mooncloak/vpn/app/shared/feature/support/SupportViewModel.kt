package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class SupportViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val api: VpnServiceApi
) : ViewModel<SupportStateModel>(initialStateValue = SupportStateModel()) {

    public fun load() {
        coroutineScope.launch {
            try {
                emit { current -> current.copy(isLoading = true) }

                emit { current ->
                    current.copy(
                        supportEmailAddress = appClientInfo.supportEmail,
                        featureRequestUri = appClientInfo.supportFeatureRequestUri,
                        issueRequestUri = appClientInfo.supportIssueUri,
                        rateAppUri = appClientInfo.rateAppUri
                    )
                }

                val pages = api.getSupportFAQPages()

                emit { current ->
                    current.copy(
                        isLoading = false,
                        faqPages = pages
                    )
                }
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error loading support settings.",
                    cause = e
                )

                emit { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }
}
