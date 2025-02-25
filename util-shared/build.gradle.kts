import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.dokka")
}

kotlin {
    applyDefaultHierarchyTemplate()

    // TODO: Re-enable: linuxArm64()
    // TODO: Re-enable: linuxX64()

    // TODO: Re-enable: mingwX64()

    // TODO: Re-enable: macosX64()
    // TODO: Re-enable: macosArm64()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    // TODO: Re-enable: tvosArm64()
    // TODO: Re-enable: tvosX64()
    // TODO: Re-enable: tvosSimulatorArm64()

    // TODO: Re-enable: watchosArm32()
    // TODO: Re-enable: watchosArm64()
    // TODO: Re-enable: watchosX64()
    // TODO: Re-enable: watchosSimulatorArm64()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

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
                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                api(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                api(KotlinX.serialization.json)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                api(KotlinX.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(KotlinX.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }
    }
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.util.shared"

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
