package com.mooncloak.vpn.app.shared.feature.dependency

import androidx.compose.runtime.Stable
import com.mikepenz.aboutlibraries.Libs
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class DependencyLicenseListViewModel @Inject public constructor(

) : ViewModel<DependencyLicenseListStateModel>(initialStateValue = DependencyLicenseListStateModel()) {

    public fun load() {
        emit(value = state.current.value.copy(isLoading = true))

        try {
            val libs = Libs.Builder()
                //.withJson(block()) TODO: Load the Libraries File
                .build()

            emit(
                value = state.current.value.copy(
                    isLoading = false,
                    libs = libs
                )
            )
        } catch (e: Exception) {
            emit(
                value = state.current.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            )
        }
    }
}
