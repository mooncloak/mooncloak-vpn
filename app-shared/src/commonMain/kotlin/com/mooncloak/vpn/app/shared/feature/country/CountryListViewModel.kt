package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.app.shared.di.FeatureScoped
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
                loadNextPage()
            }
        }
    }

    public fun loadMore() {
        coroutineScope.launch {
            mutex.withLock {
                loadNextPage()
            }
        }
    }

    public fun select(country: CountryDetails?) {
        coroutineScope.launch {
            mutex.withLock {
                emit { current ->
                    current.copy(
                        selectedCountry = country
                    )
                }
            }
        }
    }

    private suspend fun loadNextPage() {
        if (state.current.value.canAppendMore) {
            try {
                emit { current ->
                    current.copy(
                        append = LoadState.Loading
                    )
                }

                val page = getCountryPage.invoke(
                    cursor = state.current.value.lastPage?.info?.lastCursor
                )
                val countries = (state.current.value.countries + page.countries)
                    .distinctBy { it.country.code }

                emit { current ->
                    current.copy(
                        lastPage = page,
                        countries = countries,
                        append = if (page.info.hasNext == false) {
                            LoadState.Complete
                        } else {
                            LoadState.Incomplete
                        }
                    )
                }
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error loading countries.",
                    cause = e
                )

                emit { current ->
                    current.copy(
                        append = LoadState.Error(cause = e),
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }
}
