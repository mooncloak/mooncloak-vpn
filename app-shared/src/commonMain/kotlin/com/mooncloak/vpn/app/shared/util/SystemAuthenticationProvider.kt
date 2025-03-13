package com.mooncloak.vpn.app.shared.util

/**
 * A component that handles requiring system authentication. For instance, displaying a system biometric authentication
 * dialog to open the application. The credentials provided to the system are NOT accessible to the application. Not
 * all platforms supports authentication in this manner.
 */
public interface SystemAuthenticationProvider {

    /**
     * Whether the current platform supports authentication.
     */
    public val isSupported: Boolean

    /**
     * Determines whether a system authentication prompt should be displayed. If [isSupported] returns `false`, this
     * function should return `false` as well. This function may take into account user preferences.
     */
    public suspend fun shouldLaunch(): Boolean

    /**
     * Launches the system authentication prompt.
     *
     * @param [title] The title text to display in the system authentication prompt.
     *
     * @param [subtitle] The subtitle text to display in the system authentication prompt.
     *
     * @param [description] The description text to display in the system authentication prompt.
     *
     * @param [onError] The callback that is invoked when an error is encountered during authentication.
     *
     * @param [onFailed] The callback that is invoked when authentication failed.
     *
     * @param [onSuccess] The call back that is invoked when authentication succeeded.
     */
    public fun launchAuthentication(
        title: String,
        subtitle: String? = null,
        description: String? = null,
        onError: (code: Int, message: String?) -> Unit = { _, _ -> },
        onFailed: () -> Unit = {},
        onSuccess: () -> Unit
    )

    public companion object
}
