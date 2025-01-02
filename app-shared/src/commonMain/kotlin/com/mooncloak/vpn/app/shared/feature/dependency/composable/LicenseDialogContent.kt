package com.mooncloak.vpn.app.shared.feature.dependency.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.m3.util.strippedLicenseContent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.dependency_list_action_open_website
import com.mooncloak.vpn.app.shared.resource.dependency_list_label_developers
import com.mooncloak.vpn.app.shared.resource.dependency_list_label_funding
import com.mooncloak.vpn.app.shared.resource.dependency_list_label_licenses
import com.mooncloak.vpn.app.shared.resource.dependency_list_label_organization
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun LicenseDialogContent(
    library: Library,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = library.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            library.artifactVersion?.let { version ->
                Chip(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {},
                    enabled = false,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(
                        text = version,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        }

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = library.artifactId,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = SecondaryAlpha
                )
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )

        library.description?.let { description ->
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = SecondaryAlpha
                    )
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (!library.website.isNullOrBlank()) {
            library.website?.let { website ->
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    onClick = {
                        uriHandler.openUri(website)
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.dependency_list_action_open_website)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        library.organization?.let { organization ->
            Text(
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start),
                text = stringResource(Res.string.dependency_list_label_organization),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha))
            )

            ListItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable(enabled = !organization.url.isNullOrBlank()) {
                        organization.url?.let { url ->
                            uriHandler.openUri(url)
                        }
                    },
                headlineContent = {
                    Text(
                        text = organization.name
                    )
                },
                trailingContent = (@Composable {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                }).takeIf { !organization.url.isNullOrBlank() }
            )
        }

        if (library.developers.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start),
                text = stringResource(Res.string.dependency_list_label_developers),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha))
            )

            library.developers.filter { developer -> !developer.name.isNullOrBlank() }
                .forEach { developer ->
                    ListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(enabled = !developer.organisationUrl.isNullOrBlank()) {
                                developer.organisationUrl?.let { url ->
                                    uriHandler.openUri(url)
                                }
                            },
                        headlineContent = {
                            Text(
                                text = developer.name ?: ""
                            )
                        },
                        trailingContent = (@Composable {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                            )
                        }).takeIf { !developer.organisationUrl.isNullOrBlank() }
                    )
                }
        }

        if (library.licenses.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start),
                text = stringResource(Res.string.dependency_list_label_licenses),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = SecondaryAlpha
                    )
                )
            )

            library.licenses.forEach { license ->
                ListItem(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(enabled = !license.url.isNullOrBlank()) {
                            license.url?.let { url ->
                                uriHandler.openUri(url)
                            }
                        },
                    headlineContent = {
                        Text(
                            text = license.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    supportingContent = (@Composable {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = license.strippedLicenseContent ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = SecondaryAlpha
                                )
                            ),
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                    }).takeIf { license.strippedLicenseContent != null },
                    trailingContent = (@Composable {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }).takeIf { !license.url.isNullOrBlank() }
                )
            }
        }

        if (library.funding.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start),
                text = stringResource(Res.string.dependency_list_label_funding),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = SecondaryAlpha
                    )
                )
            )

            library.funding.forEach { funding ->
                ListItem(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(enabled = funding.url.isNotBlank()) {
                            funding.url.let { url ->
                                uriHandler.openUri(url)
                            }
                        },
                    headlineContent = {
                        Text(
                            text = funding.platform,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = (@Composable {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }).takeIf { funding.url.isNotBlank() }
                )
            }
        }
    }
}
