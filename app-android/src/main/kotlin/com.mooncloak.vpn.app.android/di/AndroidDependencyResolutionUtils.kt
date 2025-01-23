package com.mooncloak.vpn.app.android.di

import android.content.Context
import com.mooncloak.vpn.app.android.MooncloakVpnApplication
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import kotlin.jvm.Throws

/**
 * Retrieves the application dependency from the [ApplicationComponent] obtained from the [MooncloakVpnApplication].
 *
 * @receiver [Context] The [Context] which will be used to obtain an instance of [MooncloakVpnApplication], and then
 * the [ApplicationComponent].
 *
 * @param [resolver] The function that will resolve an application dependency, given the [ApplicationComponent] as a
 * receiver.
 *
 * @return The resolved dependency of type [R].
 */
@Throws(IllegalStateException::class)
public fun <R> Context.applicationDependency(resolver: AndroidApplicationComponent.() -> R): R {
    val application = (this.applicationContext as? MooncloakVpnApplication)
        ?: throw IllegalStateException("Cannot resolve application dependency. The Application class was not the expected '${MooncloakVpnApplication::class.qualifiedName}' class.")

    return resolver.invoke(application.applicationComponent)
}
