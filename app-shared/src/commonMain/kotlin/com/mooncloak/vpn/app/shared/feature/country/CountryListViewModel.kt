package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.country.source.CountryPager
import com.mooncloak.vpn.app.shared.feature.country.source.CountryPagerLoader
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalPaginationAPI::class)
@Stable
@FeatureScoped
public class CountryListViewModel @Inject public constructor(
    private val countryPagerLoader: CountryPagerLoader
) : ViewModel<CountryListStateModel>(initialStateValue = CountryListStateModel()) {

    private val mutex = Mutex(locked = false)

    private var countryPager: CountryPager? = null
    private var countryJob: Job? = null

    public fun load() {
        countryJob?.cancel()

        countryPager = countryPagerLoader.load(
            request = PageRequest(),
            coroutineScope = coroutineScope
        )

        countryJob = countryPager?.flow
            ?.onEach { model ->
                val countries = model.pages.flatMap { page -> page.get() }
                    .takeIf { items -> items.isNotEmpty() }

                // Prevents "Load More" button from showing when we failed to load any pages with
                // this request. The Pager component only checks if the "hasNext" property is
                // `true`, but it can also be `null`. TODO: Replace when that component is updated?
                val append = if (countries.isNullOrEmpty()) {
                    LoadState.Complete
                } else {
                    model.append
                }

                emit(
                    state.current.value.copy(
                        countries = countries ?: emptyList(),
                        prepend = model.prepend,
                        append = append,
                        refresh = model.refresh,
                        errorMessage = null
                    )
                )
            }
            ?.catch { LogPile.warning(message = "Error listening to search feed.") }
            ?.launchIn(coroutineScope)
    }

    public fun loadMore() {
        coroutineScope.launch {
            mutex.withLock {
                if (state.current.value.canAppendMore) {
                    countryPager?.append()
                }
            }
        }
    }
}
