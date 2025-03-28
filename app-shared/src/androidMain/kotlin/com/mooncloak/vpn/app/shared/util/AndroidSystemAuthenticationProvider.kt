package com.mooncloak.vpn.app.shared.util

import android.app.Activity
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.settings.AppSettings
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

public class AndroidSystemAuthenticationProvider @Inject public constructor(
    private val activity: Activity,
    private val preferencesStorage: UserPreferenceSettings,
    private val appStorage: AppSettings,
    private val clock: Clock
) : SystemAuthenticationProvider {

    private val biometricManager: BiometricManager = BiometricManager.from(activity)

    override val isSupported: Boolean
        // TODO: Consider when the user has to enroll? They technically can use the biometrics if they enroll.
        get() = biometricManager.canAuthenticate(supportedAuthenticators) == BiometricManager.BIOMETRIC_SUCCESS

    // According to the developer documentation, DEVICE_CREDENTIAL and BIOMETRIC_STRONG | DEVICE_CREDENTIAL are not
    // supported on Android 10 (API level 29) and lower. So, for those versions, we just use BIOMETRIC_STRONG.
    // https://developer.android.com/identity/sign-in/biometric-auth
    private val supportedAuthenticators: Int = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
    } else {
        BiometricManager.Authenticators.BIOMETRIC_STRONG
    }

    @OptIn(ExperimentalPersistentStateAPI::class)
    override suspend fun shouldLaunch(): Boolean {
        if (!isSupported) return false
        if (preferencesStorage.requireSystemAuth.get() != true) return false

        val lastAuthenticated = appStorage.lastAuthenticated.current.value ?: return true
        val timeout = preferencesStorage.systemAuthTimeout.get() ?: 5.minutes
        val now = clock.now()

        return now > lastAuthenticated + timeout
    }

    override fun launchAuthentication(
        title: String,
        subtitle: String?,
        description: String?,
        onError: (code: Int, message: String?) -> Unit,
        onFailed: () -> Unit,
        onSuccess: () -> Unit
    ) {
        // Implementation Notes:
        // I am deliberately not tying the crypto key with the authentication result right now, because this introduces
        // complexity around whether devices support biometric logins and not, and if/when the user changes or updates
        // their biometrics, it invalidates the associated key (which would lose all the encrypted data). So, currently
        // the key is separate from the biometric auth request. However, this may change in the future. Also note, the
        // key is still secured via the keystore.

        // Note: that you cannot combine setNegativeButtonText and setAllowedAuthenticators at the same time.
        // https://developer.android.com/identity/sign-in/biometric-auth
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setAllowedAuthenticators(supportedAuthenticators)
            .build()

        val prompt = BiometricPrompt(
            activity as FragmentActivity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    onSuccess.invoke()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                    onFailed.invoke()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                    onError.invoke(errorCode, errString.toString())
                }
            }
        )

        prompt.authenticate(promptInfo)
    }
}
