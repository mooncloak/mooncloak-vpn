package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.support.composable.DefaultSupportCard
import com.mooncloak.vpn.app.shared.feature.support.di.createSupportComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.destination_main_support_title
import com.mooncloak.vpn.app.shared.resource.support_email_action
import com.mooncloak.vpn.app.shared.resource.support_email_default_subject
import com.mooncloak.vpn.app.shared.resource.support_email_description
import com.mooncloak.vpn.app.shared.resource.support_email_title
import com.mooncloak.vpn.app.shared.resource.support_feature_request_action
import com.mooncloak.vpn.app.shared.resource.support_feature_request_description
import com.mooncloak.vpn.app.shared.resource.support_feature_request_title
import com.mooncloak.vpn.app.shared.resource.support_issue_action
import com.mooncloak.vpn.app.shared.resource.support_issue_description
import com.mooncloak.vpn.app.shared.resource.support_issue_title
import com.mooncloak.vpn.app.shared.resource.support_rate_app_action
import com.mooncloak.vpn.app.shared.resource.support_rate_app_description
import com.mooncloak.vpn.app.shared.resource.support_rate_app_title
import com.mooncloak.vpn.app.shared.util.openEmail
import org.jetbrains.compose.resources.stringResource

@Composable
public fun SupportScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createSupportComponent(
            applicationDependencies = this
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val uriHandler = LocalUriHandler.current
    val emailSubject = stringResource(Res.string.support_email_default_subject)

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = topAppBarBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Text(text = stringResource(Res.string.destination_main_support_title))
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "TopSpacing") {
                Spacer(modifier = Modifier.height(containerPaddingValues.calculateTopPadding()))
            }

            viewModel.state.current.value.supportEmailAddress?.let { supportEmailAddress ->
                item(key = "SupportEmailCard") {
                    DefaultSupportCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 12.dp),
                        title = stringResource(Res.string.support_email_title),
                        icon = Icons.Default.Email,
                        description = stringResource(Res.string.support_email_description),
                        action = stringResource(Res.string.support_email_action),
                        onAction = {
                            uriHandler.openEmail(
                                to = listOf(supportEmailAddress),
                                subject = emailSubject
                            )
                        }
                    )
                }
            }

            viewModel.state.current.value.featureRequestUri?.let { featureRequestUri ->
                item(key = "SupportFeatureRequestCard") {
                    DefaultSupportCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(Res.string.support_feature_request_title),
                        icon = Icons.Default.Add,
                        description = stringResource(Res.string.support_feature_request_description),
                        action = stringResource(Res.string.support_feature_request_action),
                        onAction = {
                            uriHandler.openUri(featureRequestUri)
                        }
                    )
                }
            }

            viewModel.state.current.value.issueRequestUri?.let { issueRequestUri ->
                item(key = "SupportIssueCard") {
                    DefaultSupportCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(Res.string.support_issue_title),
                        icon = Icons.Default.BugReport,
                        description = stringResource(Res.string.support_issue_description),
                        action = stringResource(Res.string.support_issue_action),
                        onAction = {
                            uriHandler.openUri(issueRequestUri)
                        }
                    )
                }
            }

            viewModel.state.current.value.rateAppUri?.let { rateAppUri ->
                item(key = "RateAppCard") {
                    DefaultSupportCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 12.dp),
                        title = stringResource(Res.string.support_rate_app_title),
                        icon = Icons.Default.Stars,
                        description = stringResource(Res.string.support_rate_app_description),
                        action = stringResource(Res.string.support_rate_app_action),
                        onAction = {
                            uriHandler.openUri(rateAppUri)
                        }
                    )
                }
            }

            item(key = "BottomSpacing") {
                Spacer(modifier = Modifier.height(containerPaddingValues.calculateBottomPadding()))
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
