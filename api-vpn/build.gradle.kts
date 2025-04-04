import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.dokka")
    id("mooncloak.multiplatform")
}

kotlin {
    sourceSets {
        all {
            // Disable warnings and errors related to these expected @OptIn annotations.
            // See: https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("com.chrynan.navigation.ExperimentalNavigationApi")
            languageSettings.optIn("-Xexpect-actual-classes")
        }

        val commonMain by getting {
            dependencies {
                api(project(":util-shared"))
                api(project(":data-shared"))

                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                implementation(KotlinX.serialization.json)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                api(KotlinX.datetime)

                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                api("io.ktor:ktor-client-core:_")
                api("io.ktor:ktor-client-content-negotiation:_")
                api("io.ktor:ktor-serialization-kotlinx-json:_")
                api("io.ktor:ktor-client-logging:_")
                implementation("io.ktor:ktor-client-encoding:_")

                // Core API models: apix
                // https://github.com/mooncloak/apix
                // Apache 2.0: https://github.com/mooncloak/apix/blob/main/LICENSE
                implementation("com.mooncloak.kodetools.apix:apix-core:_")

                // Logging
                // https://github.com/mooncloak/logpile
                api("com.mooncloak.kodetools.logpile:logpile-core:_")

                // Pagination Utils - pagex
                // https://github.com/mooncloak/pagex
                // Apache 2.0: https://github.com/mooncloak/pagex/blob/main/LICENSE
                implementation("com.mooncloak.kodetools.pagex:pagex-core:_")

                implementation(compose.runtime)

                // Multiplatform Key/Value Storage
                // https://github.com/russhwolf/multiplatform-settings
                // Apache 2.0: https://github.com/russhwolf/multiplatform-settings/blob/main/LICENSE.txt
                implementation(RussHWolf.multiplatformSettings.settings)
                implementation(RussHWolf.multiplatformSettings.coroutines)
                implementation(RussHWolf.multiplatformSettings.serialization)
                implementation(RussHWolf.multiplatformSettings.noArg)

                // Rich Text Utils
                // https://github.com/mooncloak/textx
                // Apache 2.0: https://github.com/mooncloak/textx/blob/main/LICENSE
                api("com.mooncloak.kodetools.textx:textx-core:_")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(KotlinX.coroutines.test)
            }
        }
    }
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.api.shared"

    defaultConfig {
        minSdk = AppConstants.Android.minSdkVersion
        targetSdk = AppConstants.Android.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            // Opt-in to experimental compose APIs
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }
}
