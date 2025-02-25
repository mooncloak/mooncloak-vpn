package com.mooncloak.vpn.util.shared.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public actual val Dispatchers.PlatformIO: CoroutineDispatcher
    get() = Default
