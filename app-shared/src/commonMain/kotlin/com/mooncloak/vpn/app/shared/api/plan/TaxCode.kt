package com.mooncloak.vpn.app.shared.api.plan

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Represents the group a product, such as a [Plan], is taxed in. For instance, different jurisdictions can charge one
 * tax rate in the "Clothing" category and charge a different tax rate for in the "School Uniforms" category. This
 * value identifies the unique category that a product belongs to for tax purposes.
 */
@Immutable
@Serializable
@JvmInline
public value class TaxCode public constructor(
    public val value: String
)
