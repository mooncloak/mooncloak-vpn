package com.mooncloak.vpn.app.android.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mooncloak.vpn.app.android.MooncloakVpnApplication
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.util.ActivityForResultLauncher
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

public abstract class BaseActivity : AppCompatActivity(),
    ActivityForResultLauncher {

    protected val applicationComponent: ApplicationComponent
        get() = (this.applicationContext as MooncloakVpnApplication).applicationComponent

    protected open val isEdgeToEdgeEnabled: Boolean = false

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            continuation?.resume(result)
        }

    private var continuation: Continuation<ActivityResult>? = null

    private val activityForResultMutex = Mutex(locked = false)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isEdgeToEdgeEnabled) {
            // According to the documentation, this should be enabled here to set up edge-to-edge with the default style.
            enableEdgeToEdge()
        }

        super.onCreate(savedInstanceState)
    }

    override suspend fun launch(intent: Intent): ActivityResult =
        withContext(Dispatchers.PlatformIO) {
            activityForResultMutex.withLock {
                suspendCancellableCoroutine { continuation ->
                    this@BaseActivity.continuation = continuation

                    activityLauncher.launch(intent)
                }
            }
        }
}
