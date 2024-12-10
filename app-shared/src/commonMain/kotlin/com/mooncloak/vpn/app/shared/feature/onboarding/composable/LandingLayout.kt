package com.mooncloak.vpn.app.shared.feature.onboarding.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_description
import com.mooncloak.vpn.app.shared.resource.app_name
import com.mooncloak.vpn.app.shared.resource.ic_logo_mooncloak
import com.mooncloak.vpn.app.shared.resource.landing_action_start
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LandingLayout(
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.padding(top = 32.dp)
                .size(96.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background),
            painter = painterResource(Res.drawable.ic_logo_mooncloak),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.app_description),
            style = MaterialTheme.typography.titleLarge.copy(color = LocalContentColor.current.copy(alpha = 0.68f)),
            maxLines = 2,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 32.dp),
            onClick = onStart
        ) {
            Text(text = stringResource(Res.string.landing_action_start))
        }
    }
}
