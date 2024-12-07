package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mooncloak.kodetools.statex.ViewModel

@ComponentScoped
public interface FeatureDependencies {

    public val viewModel: ViewModel<*>

    public companion object
}

@Composable
public fun <Dependencies : FeatureDependencies> rememberFeatureDependencies(
    component: ApplicationComponent.() -> Dependencies
): Dependencies {
    val applicationDependencies = LocalApplicationComponent.current

    return remember(applicationDependencies) {
        component.invoke(applicationDependencies)
    }
}
