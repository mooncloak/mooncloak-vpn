package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.api.shared.location.RegionDetails
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.country.model.CountryListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.RegionListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.model.ServerListLayoutStateModel
import com.mooncloak.vpn.app.shared.feature.country.usecase.GetCountryPageUseCase
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalPaginationAPI::class)
@Stable
@FeatureScoped
public class CountryListViewModel @Inject public constructor(
    private val getCountryPage: GetCountryPageUseCase
) : ViewModel<CountryListStateModel>(initialStateValue = CountryListStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                when (state.current.value.layout) {
                    is CountryListLayoutStateModel -> loadNextCountryPage()
                    is ServerListLayoutStateModel -> loadNextServerPage()
                    else -> {}
                }
            }
        }
    }

    public fun loadMore() {
        coroutineScope.launch {
            mutex.withLock {
                when (state.current.value.layout) {
                    is CountryListLayoutStateModel -> loadNextCountryPage()
                    is ServerListLayoutStateModel -> loadNextServerPage()
                    else -> {}
                }
            }
        }
    }

    public fun goBack() {
        coroutineScope.launch {
            mutex.withLock {
                val updatedLayout = when (val layout = state.current.value.layout) {
                    is CountryListLayoutStateModel -> layout
                    is RegionListLayoutStateModel -> CountryListLayoutStateModel()
                    is ServerListLayoutStateModel -> RegionListLayoutStateModel(countryDetails = layout.countryDetails)
                }

                emit { current ->
                    current.copy(layout = updatedLayout)
                }
            }
        }
    }

    public fun goTo(country: CountryDetails) {
        coroutineScope.launch {
            mutex.withLock {
                val updatedLayout = RegionListLayoutStateModel(countryDetails = country)

                emit { current ->
                    current.copy(
                        layout = updatedLayout
                    )
                }
            }
        }
    }

    public fun goTo(
        country: CountryDetails,
        region: RegionDetails
    ) {
        coroutineScope.launch {
            mutex.withLock {
                val updatedLayout = ServerListLayoutStateModel(
                    countryDetails = country,
                    regionDetails = region
                )

                emit { current ->
                    current.copy(
                        layout = updatedLayout
                    )
                }
            }
        }
    }

    public fun connectTo(country: CountryDetails) {
        coroutineScope.launch {
            mutex.withLock {
                // TODO: Connect to best server for country.
            }
        }
    }

    public fun connectTo(region: RegionDetails) {
        coroutineScope.launch {
            mutex.withLock {
                // TODO: Connect to best server for region.
            }
        }
    }

    public fun connectTo(server: Server) {
        coroutineScope.launch {
            mutex.withLock {
                // TODO: Connect to best server for region.
            }
        }
    }

    private suspend fun loadNextCountryPage() {
        val layout = (state.current.value.layout as? CountryListLayoutStateModel) ?: CountryListLayoutStateModel()

        if (!layout.append.endOfPaginationReached) {
            try {
                emit { current ->
                    current.copy(
                        layout = layout.copy(
                            append = LoadState.Loading
                        )
                    )
                }

                val page = getCountryPage.invoke(
                    cursor = layout.lastCursor
                )
                val countries = (layout.countries + page.countries)
                    .distinctBy { it.country.code }
                val append = when {
                    page.info.hasNext == false -> LoadState.Complete
                    page.countries == layout.lastPage?.countries -> LoadState.Complete
                    page.countries.isEmpty() -> LoadState.Complete
                    else -> LoadState.Incomplete
                }

                emit { current ->
                    current.copy(
                        layout = layout.copy(
                            lastPage = page,
                            countries = countries,
                            append = append
                        )
                    )
                }
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error loading countries.",
                    cause = e
                )

                emit { current ->
                    current.copy(
                        layout = layout.copy(
                            append = LoadState.Error(cause = e),
                        ),
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }

    private suspend fun loadNextServerPage() {
        val layout = state.current.value.layout

        if (layout is ServerListLayoutStateModel && !layout.append.endOfPaginationReached) {
            try {
                emit { current ->
                    current.copy(
                        layout = layout.copy(
                            append = LoadState.Loading
                        )
                    )
                }

                // TODO: Load servers for country and region
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error loading countries.",
                    cause = e
                )

                emit { current ->
                    current.copy(
                        layout = layout.copy(
                            append = LoadState.Error(cause = e),
                        ),
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }
}
