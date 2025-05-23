package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.debug
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository.ContributorRepository
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
public class CollaboratorListViewModel @Inject public constructor(
    private val collaboratorRepository: ContributorRepository
) : ViewModel<CollaboratorListStateModel>(initialStateValue = CollaboratorListStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            try {
                val collaborators = collaboratorRepository.getAll()

                LogPile.debug(tag = TAG, message = "Contributors: $collaborators")

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        isError = false,
                        collaborators = collaborators,
                        errorMessage = null
                    )
                )
            } catch (e: Exception) {
                LogPile.error(tag = TAG, message = "Error retrieving contributors.", cause = e)

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

    public companion object {

        private const val TAG: String = "CollaboratorListViewModel"
    }
}
