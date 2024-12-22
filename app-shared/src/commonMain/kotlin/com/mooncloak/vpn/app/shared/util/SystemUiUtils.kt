package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A component that is used for scoping system, or platform-specific, UI utility functions.
 */
public sealed interface SystemUi {

    public companion object : SystemUi
}

/**
 * Enables an edge-to-edge display for the current platform UI container.
 *
 * > [!Warning]
 * > Not all platforms support edge-to-edge display. In platforms where this is not supported, invoking this function
 * > performs no operation.
 *
 * > [!Warning]
 * > Different behavior may be performed on Android depending on the Android version. Refer to the
 * > androidx.activity.enableEdgeToEdge documentation for more information.
 *
 * @param [statusBarStyle] The [SystemBarStyle] for the system's status bar.
 *
 * @param [navigationBarStyle] The [SystemBarStyle] for the system's navigation bar.
 */
@Composable
public expect fun SystemUi.EnableEdgeToEdge(
    statusBarStyle: SystemBarStyle = LocalStatusBarStyle.current,
    navigationBarStyle: SystemBarStyle = LocalNavigationBarStyle.current,
    content: @Composable () -> Unit
)

/**
 * Represents the styling for the system status and navigation bars.
 *
 * > [!Note]
 * > To create an instance of this class, use one of the constructor functions: [dark], [light], or [auto].
 */
@Immutable
public class SystemBarStyle private constructor(
    public val lightScrim: Color,
    public val darkScrim: Color,
    public val mode: Mode
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SystemBarStyle) return false

        if (lightScrim != other.lightScrim) return false
        if (darkScrim != other.darkScrim) return false

        return mode == other.mode
    }

    override fun hashCode(): Int {
        var result = lightScrim.hashCode()
        result = 31 * result + darkScrim.hashCode()
        result = 31 * result + mode.hashCode()
        return result
    }

    override fun toString(): String =
        "SystemBarStyle(lightScrim=$lightScrim, darkScrim=$darkScrim, mode=$mode)"

    @Immutable
    public enum class Mode {

        Dark,
        Light,
        System
    }

    public companion object {

        /**
         * Creates a [SystemBarStyle] instance for system mode UI.
         *
         * @param [lightScrim] The scrim color to be used for the background when the app is in light mode. Note that
         * this is used only on API level 28 and below.
         *
         * @param [darkScrim] The scrim color to be used for the background when the app is in dark mode. This is also
         * used on devices where the system icon color is always light. Note that this is used only on API level 28 and
         * below.
         */
        public fun auto(
            lightScrim: Color,
            darkScrim: Color
        ): SystemBarStyle = SystemBarStyle(
            lightScrim = lightScrim,
            darkScrim = darkScrim,
            mode = Mode.System
        )


        /**
         * Creates a [SystemBarStyle] instance for dark mode UI.
         *
         * @param [scrim] The scrim color to be used for the background. It is expected to be dark for the contrast
         * against the light system icons.
         */
        public fun dark(scrim: Color): SystemBarStyle = SystemBarStyle(
            lightScrim = scrim,
            darkScrim = scrim,
            mode = Mode.Dark
        )

        /**
         * Creates a [SystemBarStyle] instance for light mode UI.
         *
         * @param [lightScrim] The scrim color to be used for the background. It is expected to be light for the
         * contrast against the dark system icons.
         *
         * @param [darkScrim] The scrim color to be used for the background on devices where the system icon color is
         * always light. It is expected to be dark.
         */
        public fun light(
            lightScrim: Color,
            darkScrim: Color = lightScrim
        ): SystemBarStyle = SystemBarStyle(
            lightScrim = lightScrim,
            darkScrim = darkScrim,
            mode = Mode.Light
        )
    }
}

internal val LocalStatusBarStyle: ProvidableCompositionLocal<SystemBarStyle> =
    staticCompositionLocalOf { DefaultStatusBar }

internal val LocalNavigationBarStyle: ProvidableCompositionLocal<SystemBarStyle> =
    staticCompositionLocalOf { DefaultNavigationBarStyle }

// The light scrim color used in the platform API 29+
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/com/android/internal/policy/DecorView.java;drc=6ef0f022c333385dba2c294e35b8de544455bf19;l=142
private val DefaultLightScrim = Color(0xe6FFFFFF)

// The dark scrim color used in the platform.
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/res/res/color/system_bar_background_semi_transparent.xml
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/res/remote_color_resources_res/values/colors.xml;l=67
private val DefaultDarkScrim = Color(0x801b1b1b)

private val DefaultStatusBar: SystemBarStyle = SystemBarStyle.auto(
    lightScrim = Color.Transparent,
    darkScrim = Color.Transparent
)

private val DefaultNavigationBarStyle: SystemBarStyle = SystemBarStyle.auto(
    lightScrim = DefaultLightScrim,
    darkScrim = DefaultDarkScrim
)
