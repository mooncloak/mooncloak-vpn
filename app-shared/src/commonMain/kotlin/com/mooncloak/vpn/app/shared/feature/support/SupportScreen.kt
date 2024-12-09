package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.LargeTopAppBar
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.app.AppClientInfo
import com.mooncloak.vpn.app.shared.app.rateAppUri
import com.mooncloak.vpn.app.shared.app.supportEmail
import com.mooncloak.vpn.app.shared.app.supportFeatureRequestUri
import com.mooncloak.vpn.app.shared.app.supportIssueUri
import com.mooncloak.vpn.app.shared.feature.support.composable.DefaultSupportCard
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
    modifier: Modifier = Modifier
) {
    val viewModel = remember { SupportViewModel() }
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
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = topAppBarBehavior,
                title = {
                    Text(text = stringResource(Res.string.destination_main_support_title))
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "SupportEmailCard") {
                DefaultSupportCard(
                    title = stringResource(Res.string.support_email_title),
                    icon = Icons.Default.Email,
                    description = stringResource(Res.string.support_email_description),
                    action = stringResource(Res.string.support_email_action),
                    onAction = {
                        uriHandler.openEmail(
                            to = listOf(AppClientInfo.supportEmail),
                            subject = emailSubject
                        )
                    }
                )
            }

            item(key = "SupportFeatureRequestCard") {
                DefaultSupportCard(
                    title = stringResource(Res.string.support_feature_request_title),
                    icon = Icons.Default.Add,
                    description = stringResource(Res.string.support_feature_request_description),
                    action = stringResource(Res.string.support_feature_request_action),
                    onAction = {
                        uriHandler.openUri(AppClientInfo.supportFeatureRequestUri)
                    }
                )
            }

            item(key = "SupportIssueCard") {
                DefaultSupportCard(
                    title = stringResource(Res.string.support_issue_title),
                    icon = Icons.Default.BugReport,
                    description = stringResource(Res.string.support_issue_description),
                    action = stringResource(Res.string.support_issue_action),
                    onAction = {
                        uriHandler.openUri(AppClientInfo.supportIssueUri)
                    }
                )
            }

            item(key = "RateAppCard") {
                DefaultSupportCard(
                    title = stringResource(Res.string.support_rate_app_title),
                    icon = Icons.Default.Stars,
                    description = stringResource(Res.string.support_rate_app_description),
                    action = stringResource(Res.string.support_rate_app_action),
                    onAction = {
                        uriHandler.openUri(AppClientInfo.rateAppUri)
                    }
                )
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
