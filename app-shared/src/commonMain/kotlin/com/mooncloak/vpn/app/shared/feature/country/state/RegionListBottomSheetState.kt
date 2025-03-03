package com.mooncloak.vpn.app.shared.feature.country.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
public class RegionListBottomSheetState internal constructor(
    internal val bottomSheetState: ManagedModalBottomSheetState
) {

    public val details: State<CountryDetails?>
        get() = mutableDetails

    private val mutableDetails = mutableStateOf<CountryDetails?>(null)

    private val mutex = Mutex(locked = false)

    public suspend fun show(country: CountryDetails) {
        mutex.withLock {
            mutableDetails.value = country
            bottomSheetState.show()
        }
    }

    public suspend fun hide() {
        mutex.withLock {
            bottomSheetState.hide()
            mutableDetails.value = null
        }
    }
}

@Composable
public fun rememberRegionListBottomSheetState(
    bottomSheetState: ManagedModalBottomSheetState = rememberManagedModalBottomSheetState()
): RegionListBottomSheetState = remember(bottomSheetState) {
    RegionListBottomSheetState(bottomSheetState = bottomSheetState)
}
