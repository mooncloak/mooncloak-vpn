package com.mooncloak.vpn.network.core.tunnel

/**
 * A component that prepares a [TunnelManager] for use before connecting to a VPN server. The actual preparation code
 * for a [TunnelManager] is platform-specific and can't be invoked from common code (ex: Android requires an Activity).
 * So this component is created as an abstraction that can be used with dependency inversion so that the common code
 * can invoke the preparation logic.
 *
 * > [!Note]
 * > This component can only be used within the Presentation level scope as the implementation on Android will require
 * > an Activity.
 */
public fun interface TunnelManagerPreparer {

    public suspend fun prepare(): Boolean

    public companion object
}
