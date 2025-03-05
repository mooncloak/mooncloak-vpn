package com.mooncloak.vpn.app.android.util

import androidx.core.graphics.drawable.IconCompat
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.android.R
import com.mooncloak.vpn.app.android.activity.QuickConnectActivity
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.util.shortcuts.AppShortcut
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider

internal class AndroidAppShortcutProvider @Inject internal constructor(
    private val activityContext: ActivityContext
) : AppShortcutProvider {

    override suspend fun get(): List<AppShortcut> =
        listOf(
            AppShortcut(
                id = "quick.connect",
                shortLabel = "Quick Connect", // TODO: Hardcoded String Resource
                intent = QuickConnectActivity.newIntent(context = activityContext),
                icon = IconCompat.createWithResource(activityContext, R.drawable.ic_quick_connect)
            )
        )
}
