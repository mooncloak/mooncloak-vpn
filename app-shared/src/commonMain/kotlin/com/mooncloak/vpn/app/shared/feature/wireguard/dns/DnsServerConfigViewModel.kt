package com.mooncloak.vpn.app.shared.feature.wireguard.dns

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.moonscape.text.TextFieldStateModel
import com.mooncloak.moonscape.text.error
import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.settings_dns_servers_error_invalid_ip
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.app.shared.util.IPv4AddressValidator
import com.mooncloak.vpn.data.shared.keyvalue.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration.Companion.milliseconds

public class DnsServerConfigViewModel @Inject public constructor(
    private val preferencesStorage: UserPreferenceSettings,
    private val ipAddressValidator: IPv4AddressValidator,
    private val onSaveCompleteListener: DnsServerOnSaveCompleteListener,
    private val userPreferenceSettings: UserPreferenceSettings
) : ViewModel<DnsServerConfigStateModel>(initialStateValue = DnsServerConfigStateModel()) {

    private val mutex = Mutex(locked = false)

    private val primaryFlow = MutableSharedFlow<TextFieldValue>()
    private val secondaryFlow = MutableSharedFlow<TextFieldValue>()

    private var primaryJob: Job? = null
    private var secondaryJob: Job? = null
    private var moonShieldJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                primaryJob?.cancel()
                secondaryJob?.cancel()
                moonShieldJob?.cancel()

                try {
                    emit(
                        value = state.current.value.copy(
                            isLoading = true
                        )
                    )

                    val preferences = preferencesStorage.wireGuard.get() ?: WireGuardPreferences()
                    val primaryDnsAddress =
                        preferences.dnsAddresses.firstOrNull() ?: WireGuardPreferences.Defaults.PrimaryDnsServer
                    val secondaryDnsAddress = preferences.dnsAddresses.toList().getOrNull(1)
                        ?: WireGuardPreferences.Defaults.SecondaryDnsServer
                    val moonShieldEnabled = userPreferenceSettings.moonShieldEnabled.get() ?: false

                    emit(
                        value = state.current.value.copy(
                            primary = TextFieldStateModel(
                                value = TextFieldValue(text = primaryDnsAddress)
                            ),
                            secondary = TextFieldStateModel(
                                value = TextFieldValue(text = secondaryDnsAddress)
                            ),
                            initialPrimary = primaryDnsAddress,
                            initialSecondary = secondaryDnsAddress,
                            moonShieldEnabled = moonShieldEnabled,
                            isLoading = false,
                            errorMessage = null
                        )
                    )
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error loading WireGuard preferences for DnsServerConfig Screen.",
                        cause = e
                    )

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    )
                }

                primaryJob = primaryFlow.onEach { value ->
                    // First emit the model with the current error message. This way there is no delay in the UI.
                    emit { current ->
                        current.copy(
                            primary = TextFieldStateModel(
                                value = value,
                                supporting = current.primary.error,
                                isError = true
                            )
                        )
                    }
                }.debounce(300.milliseconds)
                    .map { value ->
                        val result = ipAddressValidator.validate(value.text)

                        TextFieldStateModel(
                            value = value,
                            supporting = if (result.isSuccess) {
                                null
                            } else {
                                getString(Res.string.settings_dns_servers_error_invalid_ip)
                            },
                            isError = true
                        )
                    }
                    .onEach { model ->
                        emit { current ->
                            current.copy(primary = model)
                        }
                    }
                    .catch { e -> LogPile.error(message = "Error listening to primary DNS server changes.", cause = e) }
                    .launchIn(this)

                secondaryJob = secondaryFlow.onEach { value ->
                    // First emit the model with the current error message. This way there is no delay in the UI.
                    emit { current ->
                        current.copy(
                            secondary = TextFieldStateModel(
                                value = value,
                                supporting = current.secondary.error,
                                isError = true
                            )
                        )
                    }
                }.debounce(300.milliseconds)
                    .map { value ->
                        val result = ipAddressValidator.validate(value.text)

                        // Then debounce the validation
                        TextFieldStateModel(
                            value = value,
                            supporting = if (result.isSuccess) {
                                null
                            } else {
                                getString(Res.string.settings_dns_servers_error_invalid_ip)
                            },
                            isError = true
                        )
                    }
                    .onEach { model ->
                        emit { current ->
                            current.copy(secondary = model)
                        }
                    }
                    .catch { e ->
                        LogPile.error(
                            message = "Error listening to secondary DNS server changes.",
                            cause = e
                        )
                    }
                    .launchIn(this)

                moonShieldJob?.cancel()
                moonShieldJob = userPreferenceSettings.moonShieldEnabled.flow()
                    .onEach { moonShieldEnabled ->
                        emit { current ->
                            current.copy(
                                moonShieldEnabled = moonShieldEnabled ?: false
                            )
                        }
                    }
                    .catch { LogPile.error(message = "Error listening to moon shield changes.", cause = it) }
                    .flowOn(Dispatchers.Main)
                    .launchIn(coroutineScope)
            }
        }
    }

    public fun updatePrimary(value: TextFieldValue) {
        coroutineScope.launch {
            mutex.withLock {
                primaryFlow.emit(value)
            }
        }
    }

    public fun updateSecondary(value: TextFieldValue) {
        coroutineScope.launch {
            mutex.withLock {
                secondaryFlow.emit(value)
            }
        }
    }

    public fun resetPrimary() {
        coroutineScope.launch {
            mutex.withLock {
                val initial = state.current.value.initialPrimary

                primaryFlow.emit(
                    TextFieldValue(
                        text = initial,
                        selection = TextRange(index = initial.length)
                    )
                )
            }
        }
    }

    public fun resetSecondary() {
        coroutineScope.launch {
            mutex.withLock {
                val initial = state.current.value.initialSecondary

                secondaryFlow.emit(
                    TextFieldValue(
                        text = initial,
                        selection = TextRange(index = initial.length)
                    )
                )
            }
        }
    }

    public fun toggleMoonShield(active: Boolean) {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    userPreferenceSettings.moonShieldEnabled.set(value = active)
                } catch (e: Exception) {
                    LogPile.error(message = "Error toggling MoonShield.", cause = e)

                    emit { current ->
                        current.copy(
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    }
                }
            }
        }
    }

    public fun save() {
        coroutineScope.launch {
            mutex.withLock {
                val primary = state.current.value.primary
                val secondary = state.current.value.secondary

                val primaryResult = ipAddressValidator.validate(primary.value.text)
                val secondaryResult = ipAddressValidator.validate(secondary.value.text)

                if (
                    primary.error == null
                    && secondary.error == null
                    && primaryResult.isSuccess
                    && secondaryResult.isSuccess
                ) {
                    emit { current ->
                        current.copy(isSaving = true)
                    }

                    val primaryAddress = primaryResult.getOrNull() ?: primary.value.text
                    val secondaryAddress = secondaryResult.getOrNull() ?: secondary.value.text

                    preferencesStorage.wireGuard.update { current ->
                        current.copy(
                            dnsAddresses = setOf(
                                primaryAddress,
                                secondaryAddress
                            )
                        )
                    }

                    emit { current ->
                        current.copy(
                            initialPrimary = primaryAddress,
                            initialSecondary = secondaryAddress,
                            isSaving = false
                        )
                    }

                    onSaveCompleteListener.onSaved()
                } else {
                    emit { current ->
                        current.copy(
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    }
                }
            }
        }
    }
}
