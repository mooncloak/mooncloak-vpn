package com.mooncloak.vpn.app.shared.util

public operator fun SystemAuthenticationProvider.Companion.invoke(): SystemAuthenticationProvider =
    JvmSystemAuthenticationProvider()

internal class JvmSystemAuthenticationProvider internal constructor() : SystemAuthenticationProvider {

    override val isSupported: Boolean = false

    override fun shouldLaunch(): Boolean = false

    override fun launchAuthentication(
        title: String,
        subtitle: String?,
        description: String?,
        onError: (code: Int, message: String?) -> Unit,
        onFailed: () -> Unit,
        onSuccess: () -> Unit
    ) {
    }
}
