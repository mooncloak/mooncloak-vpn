package com.mooncloak.vpn.app.shared.feature.server.region

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.api.shared.location.RegionDetails
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
public class RegionServerListBottomSheetState internal constructor(
    internal val bottomSheetState: ManagedModalBottomSheetState
) {

    public val country: State<CountryDetails?>
        get() = mutableCountry

    public val region: State<RegionDetails?>
        get() = mutableRegion

    private val mutableCountry = mutableStateOf<CountryDetails?>(null)
    private val mutableRegion = mutableStateOf<RegionDetails?>(null)

    private val mutex = Mutex(locked = false)

    public suspend fun show(country: CountryDetails, region: RegionDetails) {
        mutex.withLock {
            mutableCountry.value = country
            mutableRegion.value = region
            bottomSheetState.show()
        }
    }

    public suspend fun hide() {
        mutex.withLock {
            bottomSheetState.hide()
            mutableCountry.value = null
        }
    }
}

@Composable
public fun rememberRegionServerListBottomSheetState(
    bottomSheetState: ManagedModalBottomSheetState = rememberManagedModalBottomSheetState()
): RegionServerListBottomSheetState = remember(bottomSheetState) {
    RegionServerListBottomSheetState(bottomSheetState = bottomSheetState)
}
