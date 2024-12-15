package com.mooncloak.vpn.app.shared.feature.onboarding.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.onboarding_action_skip
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_accounts
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_data_creeps
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_data_selling
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_subscriptions
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_tracking
import com.mooncloak.vpn.app.shared.resource.onboarding_description_payment_crypto
import com.mooncloak.vpn.app.shared.resource.onboarding_description_payment_google_play
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_accounts
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_data_creeps
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_data_selling
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_subscriptions
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_tracking
import com.mooncloak.vpn.app.shared.resource.onboarding_title_payment_crypto
import com.mooncloak.vpn.app.shared.resource.onboarding_title_payment_google_play
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.theme.TertiaryAlpha
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnboardingLayout(
    onFinish: () -> Unit,
    isGooglePlayBuild: Boolean,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        OnboardingScreenItem(
            icon = { rememberVectorPainter(Icons.Default.CloudOff) },
            title = { stringResource(Res.string.onboarding_title_no_tracking) },
            description = { stringResource(Res.string.onboarding_description_no_tracking) }
        ),
        OnboardingScreenItem(
            icon = { rememberVectorPainter(Icons.Default.PersonOff) },
            title = { stringResource(Res.string.onboarding_title_no_accounts) },
            description = { stringResource(Res.string.onboarding_description_no_accounts) }
        ),
        OnboardingScreenItem(
            icon = { rememberVectorPainter(Icons.Default.VisibilityOff) },
            title = { stringResource(Res.string.onboarding_title_no_data_creeps) },
            description = { stringResource(Res.string.onboarding_description_no_data_creeps) }
        ),
        OnboardingScreenItem(
            icon = { rememberVectorPainter(Icons.Default.MoneyOff) },
            title = { stringResource(Res.string.onboarding_title_no_data_selling) },
            description = { stringResource(Res.string.onboarding_description_no_data_selling) }
        ),
        OnboardingScreenItem(
            icon = { rememberVectorPainter(Icons.Default.Subscriptions) },
            title = { stringResource(Res.string.onboarding_title_no_subscriptions) },
            description = { stringResource(Res.string.onboarding_description_no_subscriptions) }
        ),
        if (isGooglePlayBuild) {
            OnboardingScreenItem(
                icon = { rememberVectorPainter(Icons.Default.PlayArrow) },
                title = { stringResource(Res.string.onboarding_title_payment_google_play) },
                description = { stringResource(Res.string.onboarding_description_payment_google_play) }
            )
        } else {
            OnboardingScreenItem(
                icon = { rememberVectorPainter(Icons.Default.CurrencyBitcoin) },
                title = { stringResource(Res.string.onboarding_title_payment_crypto) },
                description = { stringResource(Res.string.onboarding_description_payment_crypto) }
            )
        }
    )

    val pagerState = rememberPagerState(pageCount = { items.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onFinish,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(text = stringResource(Res.string.onboarding_action_skip))
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            state = pagerState
        ) { pageIndex ->
            val item = items[pageIndex]

            OnboardScreen(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                icon = item.icon(),
                title = item.title(),
                description = item.description()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val hasPrevious = derivedStateOf { pagerState.currentPage > 0 }
            val previousContainerColor = animateColorAsState(
                targetValue = if (hasPrevious.value) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            val previousContentColor = animateColorAsState(
                targetValue = if (hasPrevious.value) {
                    contentColorFor(FloatingActionButtonDefaults.containerColor)
                } else {
                    LocalContentColor.current.copy(alpha = 0.68f)
                }
            )
            val previousElevation = animateDpAsState(
                targetValue = if (hasPrevious.value) {
                    6.dp
                } else {
                    0.dp
                }
            )
            val previousHoverElevation = animateDpAsState(
                targetValue = if (hasPrevious.value) {
                    8.dp
                } else {
                    0.dp
                }
            )

            FloatingActionButton(
                onClick = {
                    if (hasPrevious.value) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                containerColor = previousContainerColor.value,
                contentColor = previousContentColor.value,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = previousElevation.value,
                    pressedElevation = previousHoverElevation.value,
                    focusedElevation = previousElevation.value,
                    hoveredElevation = previousHoverElevation.value
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IndicatorList(
                size = pagerState.pageCount,
                selectedIndex = pagerState.currentPage,
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.weight(1f))

            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onFinish()
                        }
                    }
                }
            ) {
                AnimatedContent(
                    targetState = if (pagerState.currentPage < pagerState.pageCount - 1) {
                        Icons.AutoMirrored.Default.ArrowForward
                    } else {
                        Icons.Default.Start
                    }
                ) { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun IndicatorList(
    size: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        repeat(size) { currentIndex ->
            Indicator(selected = currentIndex == selectedIndex)
        }
    }
}

@Composable
private fun Indicator(
    selected: Boolean
) {
    val width = animateDpAsState(
        targetValue = if (selected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = TertiaryAlpha)
                }
            )
    )
}

@Composable
private fun HeaderGraphic(
    painter: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    backgroundBrush: Brush = Brush.linearGradient(
        colors = listOf(
            ColorPalette.MooncloakDarkPrimary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    ),
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    contentDescription: String? = null
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.matchParentSize()
                .clip(shape)
                .background(backgroundBrush)
        )

        val iconSize = minOf(maxWidth, maxHeight) / 2

        Icon(
            modifier = Modifier.size(iconSize),
            painter = painter,
            contentDescription = contentDescription,
            tint = contentColor
        )
    }
}

@Composable
private fun OnboardScreen(
    icon: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        HeaderGraphic(
            painter = icon,
            modifier = Modifier.size(128.dp)
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 2,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = description,
            style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current.copy(alpha = 0.68f)),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Immutable
private data class OnboardingScreenItem(
    val title: @Composable () -> String,
    val description: @Composable () -> String,
    val icon: @Composable () -> Painter
)
