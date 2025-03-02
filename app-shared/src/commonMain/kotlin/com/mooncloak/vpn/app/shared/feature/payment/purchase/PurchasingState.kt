package com.mooncloak.vpn.app.shared.feature.payment.purchase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class PurchasingState internal constructor(
    initialPurchasing: Boolean
) {

    public val purchasing: State<Boolean>
        get() = mutablePurchasing

    private val mutablePurchasing = mutableStateOf(initialPurchasing)

    private val mutex = Mutex(locked = false)

    internal suspend fun togglePurchasing(purchasing: Boolean) {
        mutex.withLock {
            mutablePurchasing.value = purchasing
        }
    }
}

@Composable
public fun rememberPurchasingState(): PurchasingState = remember { PurchasingState(initialPurchasing = false) }
