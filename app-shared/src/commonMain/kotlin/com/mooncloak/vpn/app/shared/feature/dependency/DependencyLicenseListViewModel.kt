package com.mooncloak.vpn.app.shared.feature.dependency

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
public class DependencyLicenseListViewModel @Inject public constructor(
    private val libsLoader: LibsLoader
) : ViewModel<DependencyLicenseListStateModel>(initialStateValue = DependencyLicenseListStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            try {
                val libs = libsLoader.load()
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
                        errorMessage = e.message ?: getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }
}
