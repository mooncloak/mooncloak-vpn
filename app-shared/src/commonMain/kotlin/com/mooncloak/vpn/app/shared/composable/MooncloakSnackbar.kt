package com.mooncloak.vpn.app.shared.composable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.model.NotificationStateModel

public suspend fun SnackbarHostState.showError(
    notification: NotificationStateModel,
    actionLabel: String? = null,
    duration: SnackbarDuration = if (actionLabel == null) {
        SnackbarDuration.Short
    } else {
        SnackbarDuration.Indefinite
    },
    withDismissAction: Boolean = false,
    exception: Exception? = null
): SnackbarResult = this.showError(
    message = notification.message,
    actionLabel = actionLabel,
    duration = duration,
    withDismissAction = withDismissAction,
    exception = exception
)

public suspend fun SnackbarHostState.showSuccess(
    notification: NotificationStateModel,
    actionLabel: String? = null,
    duration: SnackbarDuration = if (actionLabel == null) {
        SnackbarDuration.Short
    } else {
        SnackbarDuration.Indefinite
    },
    withDismissAction: Boolean = false
): SnackbarResult = this.showSuccess(
    message = notification.message,
    actionLabel = actionLabel,
    duration = duration,
    withDismissAction = withDismissAction
)

public suspend fun SnackbarHostState.showError(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = if (actionLabel == null) {
        SnackbarDuration.Short
    } else {
        SnackbarDuration.Indefinite
    },
    withDismissAction: Boolean = false,
    exception: Exception? = null
): SnackbarResult {
    val error = Notification.Error(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
        withDismissAction = withDismissAction,
        exception = exception
    )

    return this.showSnackbar(visuals = error)
}

public suspend fun SnackbarHostState.showSuccess(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = if (actionLabel == null) {
        SnackbarDuration.Short
    } else {
        SnackbarDuration.Indefinite
    },
    withDismissAction: Boolean = false
): SnackbarResult {
    val notification = Notification.Success(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
        withDismissAction = withDismissAction
    )

    return this.showSnackbar(visuals = notification)
}

@Composable
public fun MooncloakSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    val containerColor = rememberUpdatedState(
        when (snackbarData.visuals) {
            is Notification.Error -> MaterialTheme.colorScheme.errorContainer
            is Notification.Success -> MaterialTheme.colorScheme.tertiaryContainer
            else -> SnackbarDefaults.color
        }
    )
    val contentColor = rememberUpdatedState(
        when (snackbarData.visuals) {
            is Notification.Error -> MaterialTheme.colorScheme.onErrorContainer
            is Notification.Success -> MaterialTheme.colorScheme.onTertiaryContainer
            else -> SnackbarDefaults.contentColor
        }
    )

    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        containerColor = containerColor.value,
        contentColor = contentColor.value
    )
}

@Immutable
private sealed class Notification : SnackbarVisuals {

    @Immutable
    data class Error(
        override val message: String,
        override val actionLabel: String?,
        override val duration: SnackbarDuration,
        override val withDismissAction: Boolean,
        val exception: Exception? = null
    ) : Notification()

    @Immutable
    data class Success(
        override val message: String,
        override val actionLabel: String?,
        override val duration: SnackbarDuration,
        override val withDismissAction: Boolean
    ) : Notification()
}
