package com.mooncloak.vpn.api.shared.tunnel

import android.app.Activity

/**
 * Creates a [TunnelManagerPreparer] instance with the provided [TunnelManager] and [Activity]. The resulting
 * [TunnelManagerPreparer]'s [TunnelManagerPreparer.prepare] function, calls back to the provided
 * [TunnelManager.prepare] function with the provided [Activity] as the Context.
 *
 * @param [tunnelManager] The [TunnelManager] that will be prepared by the resulting [TunnelManagerPreparer].
 *
 * @param [activity] The [Activity] that will be passed to the [TunnelManager.prepare] function when the resulting
 * [TunnelManagerPreparer.prepare] function is invoked.
 *
 * @return A [TunnelManagerPreparer] instance that will prepare the provided [TunnelManager].
 */
public operator fun TunnelManagerPreparer.Companion.invoke(
    tunnelManager: TunnelManager,
    activity: Activity
): TunnelManagerPreparer = AndroidTunnelManagerPreparer(
    tunnelManager = tunnelManager,
    activity = activity
)

internal class AndroidTunnelManagerPreparer internal constructor(
    private val tunnelManager: TunnelManager,
    private val activity: Activity
) : TunnelManagerPreparer {

    override suspend fun prepare(): Boolean = tunnelManager.prepare(context = activity)
}
