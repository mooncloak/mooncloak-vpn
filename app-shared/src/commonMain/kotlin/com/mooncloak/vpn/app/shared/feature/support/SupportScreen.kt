package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.MooncloakSnackbar
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.app.shared.composable.showError
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.support.composable.DefaultSupportCard
import com.mooncloak.vpn.app.shared.feature.support.composable.FAQHeader
import com.mooncloak.vpn.app.shared.feature.support.composable.FAQQuestionCard
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_bug_report
import com.mooncloak.vpn.app.shared.resource.cd_feature_request
import com.mooncloak.vpn.app.shared.resource.cd_open_locations
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
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import com.mooncloak.vpn.app.shared.util.openEmail
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
public fun SupportScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createSupportComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val uriHandler = LocalUriHandler.current
    val emailSubject = stringResource(Res.string.support_email_default_subject)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MooncloakSnackbar(
                        snackbarData = snackbarData
                    )
                }
            )
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
                },
                actions = {
                    viewModel.state.current.value.issueRequestUri?.let { issueUri ->
                        AnimatedVisibility(
                            modifier = Modifier,
                            visible = topAppBarState.collapsedFraction >= 0.9f,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            TooltipBox(
                                text = stringResource(Res.string.cd_bug_report)
                            ) {
                                IconButton(
                                    modifier = Modifier.clip(CircleShape),
                                    onClick = {
                                        uriHandler.openUri(issueUri)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = stringResource(Res.string.cd_bug_report),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }

                    viewModel.state.current.value.featureRequestUri?.let { featureRequestUri ->
                        AnimatedVisibility(
                            modifier = Modifier,
                            visible = topAppBarState.collapsedFraction >= 0.9f,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            TooltipBox(
                                text = stringResource(Res.string.cd_feature_request)
                            ) {
                                IconButton(
                                    modifier = Modifier.clip(CircleShape),
                                    onClick = {
                                        uriHandler.openUri(featureRequestUri)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(Res.string.cd_feature_request),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = DefaultHorizontalPageSpacing),
                state = lazyStaggeredGridState,
                columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPageSpacing),
                verticalItemSpacing = 12.dp
            ) {
                item(
                    key = "TopSpacing",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateTopPadding()))
                }

                viewModel.state.current.value.supportEmailAddress?.let { supportEmailAddress ->
                    item(key = "SupportEmailCard") {
                        DefaultSupportCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth(),
                            title = stringResource(Res.string.support_email_title),
                            icon = Icons.Default.Email,
                            description = stringResource(Res.string.support_email_description),
                            action = stringResource(Res.string.support_email_action),
                            onAction = {
                                coroutineScope.launch {
                                    uriHandler.openEmail(
                                        to = listOf(supportEmailAddress),
                                        subject = emailSubject
                                    )
                                }
                            }
                        )
                    }
                }

                viewModel.state.current.value.featureRequestUri?.let { featureRequestUri ->
                    item(key = "SupportFeatureRequestCard") {
                        DefaultSupportCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth(),
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
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth(),
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
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth(),
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

                if (viewModel.state.current.value.faqPages.isNotEmpty()) {
                    item(
                        key = "SupportFAQHeader",
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        FAQHeader(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 32.dp, bottom = 16.dp)
                        )
                    }

                    items(
                        items = viewModel.state.current.value.faqPages.flatMap { it.mainEntity },
                        contentType = { "FAQQuestionCard" }
                    ) { question ->
                        FAQQuestionCard(
                            modifier = Modifier.fillMaxWidth(),
                            question = question
                        )
                    }
                }

                item(
                    key = "BottomSpacing",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateBottomPadding() + 28.dp))
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewModel.state.current.value.isLoading
            ) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showError(notification = NotificationStateModel(message = errorMessage))
        }
    }
}
