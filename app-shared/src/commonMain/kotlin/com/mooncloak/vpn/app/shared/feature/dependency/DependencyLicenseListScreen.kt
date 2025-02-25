package com.mooncloak.vpn.app.shared.feature.dependency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.util.htmlReadyLicenseContent
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.dependency.composable.LicenseDialog
import com.mooncloak.vpn.app.shared.feature.dependency.composable.NoDependenciesFoundLayout
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.dependency_list_description
import com.mooncloak.vpn.app.shared.resource.dependency_list_header
import org.jetbrains.compose.resources.stringResource

@Composable
public fun DependencyLicenseListScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createDependencyLicenseListComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val openDialog = remember { mutableStateOf<Library?>(null) }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    BottomSheetLayout(
        modifier = modifier,
        title = stringResource(Res.string.dependency_list_header),
        description = stringResource(Res.string.dependency_list_description),
        loadingState = derivedStateOf { viewModel.state.current.value.isLoading },
        snackbarHostState = snackbarHostState
    ) {
        AnimatedVisibility(
            visible = viewModel.state.current.value.libs != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LibrariesContainer(
                libraries = viewModel.state.current.value.libs,
                modifier = Modifier.fillMaxSize(),
                colors = LibraryDefaults.libraryColors(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onLibraryClick = { library ->
                    val license = library.licenses.firstOrNull()

                    if (!license?.htmlReadyLicenseContent.isNullOrBlank()) {
                        openDialog.value = library
                    } else if (!license?.url.isNullOrBlank()) {
                        license?.url?.also {
                            try {
                                uriHandler.openUri(it)
                            } catch (t: Throwable) {
                                LogPile.error("Failed to open url: $it")
                            }
                        }
                    }
                }
            )
        }

        AnimatedVisibility(
            visible = !viewModel.state.current.value.isLoading && viewModel.state.current.value.libs == null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NoDependenciesFoundLayout(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 32.dp)
            )
        }
    }

    openDialog.value?.let { library ->
        LicenseDialog(
            library = library,
            onDismiss = {
                openDialog.value = null
            }
        )
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
