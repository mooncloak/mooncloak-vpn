package com.mooncloak.vpn.app.shared.util.log

import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.kodetools.logpile.core.Severity
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject

/**
 * A [Logger] implementation that does not log and performs no operation when the [Logger.log] function is invoked.
 * This is provided as a way to prevent logging in production.
 */
public data object NoOpLogger : Logger {

    override fun log(
        severity: Severity,
        timestamp: Instant,
        tag: String?,
        message: String?,
        cause: Throwable?,
        metadata: JsonObject?
    ) {
        // No Operation
    }
}
