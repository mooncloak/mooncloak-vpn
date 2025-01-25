package com.mooncloak.vpn.app.android.storage

import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.app.shared.util.ApplicationContext

internal class EncryptedSharedPreferenceProvider @Inject internal constructor(
    private val context: ApplicationContext
) {

    internal fun get(fileName: String) =
        try {
            getSharedPreferences(fileName)
        } catch (e: Exception) {
            LogPile.error(
                tag = TAG,
                message = "Error retrieving encrypted shared preferences. The file might be corrupted. Attempting to clear the file and try again. WARNING: This will remove all data.",
                cause = e
            )

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                context.deleteSharedPreferences(fileName)
            }

            getSharedPreferences(fileName)
        }

    private fun getSharedPreferences(fileName: String): SharedPreferences = EncryptedSharedPreferences.create(
        fileName,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    internal companion object {

        private const val TAG: String = "EncryptedSharedPreferenceProvider"
    }
}
