package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

public interface DependencyResolverScope : ApplicationDependencies,
    PresentationDependencies {

    public companion object
}

@Composable
public fun <T> rememberDependency(getter: DependencyResolverScope.() -> T): T {
    val resolver = rememberDependencyResolverScope()

    return remember(resolver) { getter.invoke(resolver) }
}

@Composable
internal fun rememberDependencyResolverScope(): DependencyResolverScope {
    val applicationDependencies = LocalApplicationComponent.current
    val presentationDependencies = LocalPresentationComponent.current

    return remember(applicationDependencies, presentationDependencies) {
        DependencyResolverScopeImpl(
            applicationDependencies = applicationDependencies,
            presentationDependencies = presentationDependencies
        )
    }
}

private class DependencyResolverScopeImpl(
    private val applicationDependencies: ApplicationDependencies,
    private val presentationDependencies: PresentationDependencies
) : DependencyResolverScope,
    ApplicationDependencies by applicationDependencies,
    PresentationDependencies by presentationDependencies
