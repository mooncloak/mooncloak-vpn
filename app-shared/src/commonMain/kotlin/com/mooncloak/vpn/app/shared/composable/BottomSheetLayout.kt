package com.mooncloak.vpn.app.shared.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun BottomSheetLayout(
    title: String? = null,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    loadingState: State<Boolean> = mutableStateOf(false),
    snackbarAlignment: Alignment = Alignment.TopCenter, // Defaults to top because bottom sheet might not be displaying the bottom of its content when the error shows.
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarHost: @Composable () -> Unit = { SnackbarHost(hostState = snackbarHostState) },
    loading: @Composable BoxScope.() -> Unit = {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = loadingState.value
        ) {
            CircularProgressIndicator()
        }
    },
    titleSpacing: Dp = 32.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.sizeIn(minHeight = 200.dp)
            .then(modifier),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!title.isNullOrBlank() || !description.isNullOrBlank()) {
                    BottomSheetHeader(
                        modifier = Modifier.fillMaxWidth(),
                        title = title,
                        description = description,
                        icon = icon
                    )

                    Spacer(modifier = Modifier.height(titleSpacing))
                }

                content.invoke(this)
            }

            loading.invoke(this)

            Box(
                modifier = Modifier.align(snackbarAlignment)
            ) {
                snackbarHost.invoke()
            }
        }
    }
}
