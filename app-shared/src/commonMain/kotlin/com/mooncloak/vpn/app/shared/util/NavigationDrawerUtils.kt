package com.mooncloak.vpn.app.shared.util

import androidx.compose.material3.DrawerState

public suspend fun DrawerState.toggle() {
    if (this.isClosed) {
        this.open()
    } else {
        this.close()
    }
}
