package com.mooncloak.vpn.app.android.api.wireguard

import android.app.Activity
import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPairResolver
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.api.server.usecase.RegisterClientUseCase
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.app.android.activity.VPNPreparationActivity
import com.mooncloak.vpn.app.android.service.MooncloakVpnService
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.network.core.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.network.core.tunnel.Tunnel
import com.wireguard.android.backend.GoBackend
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class AndroidWireGuardTunnelManager @Inject internal constructor(
    private val backend: WireGuardBackend,
    private val connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
    private val registerClient: RegisterClientUseCase,
    private val preferencesStorage: UserPreferenceSettings,
    private val deviceIPAddressProvider: PublicDeviceIPAddressProvider,
    private val api: VpnServiceApi
) : TunnelManager {

    override val tunnels: StateFlow<List<Tunnel>>
        get() = mutableTunnels.asStateFlow()

    private val mutableTunnels = MutableStateFlow<List<Tunnel>>(emptyList())

    private val tunnelMap = mutableMapOf<String, WireGuardTunnel>()

    private val prepareMutex = Mutex(locked = false)
    private val tunnelMutex = Mutex(locked = false)
    private val emitMutex = Mutex(locked = false)

    private var continuation: CancellableContinuation<Unit>? = null

    override suspend fun prepare(context: Context): Boolean =
        prepareMutex.withLock {
            val prepareIntent = GoBackend.VpnService.prepare(context) ?: return true

            return when (context) {
                is AppCompatActivity -> suspendCancellableCoroutine { continuation ->
                    val launcher =
                        context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                            continuation.resume(result.resultCode == Activity.RESULT_OK)
                        }

                    launcher.launch(prepareIntent)
                }

                is Activity -> {
                    suspendCancellableCoroutine { continuation ->
                        this.continuation = continuation

                        context.startActivityForResult(
                            prepareIntent,
                            MooncloakVpnService.RequestCode.PREPARE
                        )
                    }

                    GoBackend.VpnService.prepare(context) == null
                }

                else -> {
                    suspendCancellableCoroutine { continuation ->
                        this.continuation = continuation

                        context.startActivity(VPNPreparationActivity.newIntent(context))
                    }

                    GoBackend.VpnService.prepare(context) == null
                }
            }
        }

    override fun finishedPreparation() {
        continuation?.resume(Unit)
        continuation = null
    }

    override suspend fun sync() {
        tunnelMutex.withLock {
            withContext(Dispatchers.IO) {
                val updatedTunnels = mutableMapOf<String, WireGuardTunnel>()

                for (tunnelName in backend.runningTunnelNames) {
                    val tunnel = tunnelMap[tunnelName] ?: WireGuardTunnel(tunnelName = tunnelName)

                    updatedTunnels[tunnelName] = tunnel.update()
                }

                tunnelMap.clear()
                tunnelMap.putAll(updatedTunnels)
            }

            emitLatest()
        }
    }

    override suspend fun connect(server: Server): Tunnel =
        tunnelMutex.withLock {
            withContext(Dispatchers.IO) {
                val tunnel = WireGuardTunnel(
                    tunnelName = server.name,
                    server = server
                )

                // FIXME: Cast
                val keyPair = connectionKeyPairResolver.resolve() as AndroidWireGuardConnectionKeyPair

                val client = registerClient(
                    serverId = server.id,
                    publicKey = keyPair.publicKey
                )
                val wireGuardPreferences = preferencesStorage.wireGuard.get() ?: WireGuardPreferences()

                val wireGuardTunnel = tunnel.toWireGuardTunnel()
                val wireGuardConfig = server.toWireGuardConfig(
                    keyPair = keyPair.keyPair,
                    client = client,
                    wireGuardPreferences = wireGuardPreferences,
                    moonShieldEnabled = preferencesStorage.moonShieldEnabled.get() ?: false
                )

                backend.setState(
                    wireGuardTunnel,
                    com.wireguard.android.backend.Tunnel.State.UP,
                    wireGuardConfig
                )

                tunnelMap[wireGuardTunnel.tunnelName] = wireGuardTunnel

                refreshDNS()

                deviceIPAddressProvider.invalidate()

                emitLatest()

                return@withContext tunnel
            }
        }

    override suspend fun disconnect(tunnelName: String) {
        tunnelMutex.withLock {
            withContext(Dispatchers.IO) {
                val tunnel = WireGuardTunnel(tunnelName = tunnelName)
                val wireGuardTunnel = tunnel.toWireGuardTunnel()

                backend.setState(
                    wireGuardTunnel,
                    com.wireguard.android.backend.Tunnel.State.DOWN,
                    null
                )

                deviceIPAddressProvider.invalidate()
            }

            emitLatest()
        }
    }

    override suspend fun disconnectAll() {
        tunnelMutex.withLock {
            withContext(Dispatchers.IO) {
                for (tunnelName in backend.runningTunnelNames) {
                    val tunnel = tunnelMap[tunnelName] ?: WireGuardTunnel(tunnelName = tunnelName)

                    LogPile.info(tag = TAG, message = "Disconnecting from tunnel '${tunnel.tunnelName}'.")

                    try {
                        backend.setState(
                            tunnel,
                            com.wireguard.android.backend.Tunnel.State.DOWN,
                            null
                        )
                    } catch (e: Exception) {
                        LogPile.error(
                            tag = TAG,
                            message = "Error disconnecting from tunnel '${tunnel.tunnelName}'.",
                            cause = e
                        )
                    }

                    deviceIPAddressProvider.invalidate()
                }
            }

            emitLatest()
        }
    }

    override fun subscribeToChanges(coroutineScope: CoroutineScope, poll: Duration): Job =
        coroutineScope.launch {
            while (this.isActive) {
                sync()

                delay(poll)
            }
        }

    private suspend fun refreshDNS() {
        // FIXME: Force refresh of DNS after connecting to VPN.
        // Super hacky solution. But the first request always fails due to a timeout. This failure forces the HTTP
        // client to clear its caches and subsequent requests will succeed. But the first request seemingly always has
        // to fail first before the subsequent requests can pass. So, we make a sure lived request so that it can fail,
        // triggering the refresh so other requests can run correctly. Also, we delay for an arbitrary amount of time
        // to hopefully allow the VPN to be connected if it isn't already.
        delay(1.seconds)
        runCatching {
            api.getReflection(
                connectionTimeout = 1.seconds,
                socketTimeout = 1.seconds,
                requestTimeout = 1.seconds
            )
        }
    }

    private suspend fun emitLatest() {
        emitMutex.withLock {
            withContext(Dispatchers.Main) {
                mutableTunnels.value = tunnelMap.values.toList()
            }
        }
    }

    private fun Tunnel.toWireGuardTunnel(): WireGuardTunnel =
        (this as? WireGuardTunnel)
            ?: tunnelMap[this.tunnelName]
            ?: WireGuardTunnel(tunnelName = this.tunnelName)

    private fun WireGuardTunnel.update(): WireGuardTunnel {
        val state = backend.getState(this)
        val stats = backend.getStatistics(this)

        this.onStateChange(state)
        this.onStatisticsChanged(stats)

        return this
    }

    internal companion object {

        private const val TAG: String = "WireGuardTunnelManager"
    }
}
