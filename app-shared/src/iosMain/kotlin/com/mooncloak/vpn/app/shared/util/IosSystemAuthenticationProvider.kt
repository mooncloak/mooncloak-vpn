package com.mooncloak.vpn.app.shared.util

public operator fun SystemAuthenticationProvider.Companion.invoke(): SystemAuthenticationProvider =
    IosSystemAuthenticationProvider()

internal class IosSystemAuthenticationProvider internal constructor() : SystemAuthenticationProvider {

    override val isSupported: Boolean = false

    override suspend fun shouldLaunch(): Boolean = false

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
