package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.feature.collaborator.repository.CollaboratorRepository
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
public class CollaboratorListViewModel @Inject public constructor(
    private val collaboratorRepository: CollaboratorRepository
) : ViewModel<CollaboratorListStateModel>(initialStateValue = CollaboratorListStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            try {
                val collaborators = collaboratorRepository.getAll()

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        isError = false,
                        collaborators = collaborators,
                        errorMessage = null
                    )
                )
            } catch (e: Exception) {
                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: getString(Res.string.global_unexpected_error),
                        isError = true
                    )
                )
            }
        }
    }
}
