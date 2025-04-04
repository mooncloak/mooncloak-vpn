package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mooncloak.vpn.app.shared.theme.MooncloakTheme

@Composable
@Preview(
    widthDp = 1400,
    heightDp = 400
)
private fun PreviewMarketingLandingLayout(){
    MooncloakTheme {
        MarketingLandingLayout(modifier = Modifier.fillMaxSize())
    }
}
