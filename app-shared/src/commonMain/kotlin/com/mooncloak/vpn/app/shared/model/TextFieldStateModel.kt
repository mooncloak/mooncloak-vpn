package com.mooncloak.vpn.app.shared.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Allows associating extra data with a [TextFieldValue], such as an [error] message.
 *
 * @property [value] The [TextFieldValue] associated with the UI component.
 *
 * @property [error] The error message associated with the UI component. This allows error messages for individual
 * Text Input UI components.
 */
@Immutable
public data class TextFieldStateModel public constructor(
    val value: TextFieldValue = TextFieldValue(),
    val error: String? = null
)
