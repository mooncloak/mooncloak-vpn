package com.mooncloak.vpn.app.shared.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public actual val Dispatchers.PlatformIO: CoroutineDispatcher
    get() = Default
