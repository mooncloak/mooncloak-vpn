package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.onboarding.di.createOnboardingComponent

@Composable
public fun OnboardingScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createOnboardingComponent(
            applicationDependencies = this
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
}
