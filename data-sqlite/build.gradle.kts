import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.dokka")
    id("app.cash.sqldelight")
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
            languageSettings.optIn("-Xexpect-actual-classes")
        }

        val commonMain by getting {
            dependencies {
                api(project(":data-shared"))

                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                implementation(KotlinX.serialization.json)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                implementation(KotlinX.datetime)

                implementation(compose.runtime)

                // Multiplatform Key/Value Storage
                // https://github.com/russhwolf/multiplatform-settings
                // Apache 2.0: https://github.com/russhwolf/multiplatform-settings/blob/main/LICENSE.txt
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                implementation(RussHWolf.multiplatformSettings.coroutines)
                implementation(RussHWolf.multiplatformSettings.serialization)

                // Database - Sqlite - SqlDelight
                // https://sqldelight.github.io/sqldelight/2.0.2/multiplatform_sqlite/
                api("app.cash.sqldelight:coroutines-extensions:_")
                api("app.cash.sqldelight:primitive-adapters:_")

                // Logging
                // https://github.com/mooncloak/logpile
                implementation("com.mooncloak.kodetools.logpile:logpile-core:_")
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
                // Database - Sqlite - SqlDelight
                // https://sqldelight.github.io/sqldelight/2.0.2/multiplatform_sqlite/
                api("app.cash.sqldelight:android-driver:_")
            }
        }

        val jvmMain by getting {
            dependencies {
                // Database - Sqlite - SqlDelight
                // https://sqldelight.github.io/sqldelight/2.0.2/multiplatform_sqlite/
                api("app.cash.sqldelight:sqlite-driver:_")
            }
        }

        val nativeMain by getting {
            dependencies {
                // Database - Sqlite - SqlDelight
                // https://sqldelight.github.io/sqldelight/2.0.2/multiplatform_sqlite/
                api("app.cash.sqldelight:native-driver:_")
            }
        }

        val iosMain by getting {
            dependencies {
                // Database - Sqlite - SqlDelight
                // https://sqldelight.github.io/sqldelight/2.0.2/multiplatform_sqlite/
                api("app.cash.sqldelight:native-driver:_")
            }
        }
    }
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.data.sqlite"

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

/**
 * Creates and defines the core app database. This uses the SqlDelight library and Sqlite.
 *
 * @see [SqlDelight Documentation](https://sqldelight.github.io/sqldelight/2.0.2/multiplatform_sqlite/)
 */
sqldelight {
    databases {
        create("MooncloakDatabase") {
            packageName.set("com.mooncloak.vpn.data.sqlite.database")
            srcDirs("src/commonMain/sqldelight")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            verifyMigrations.set(false)
        }
    }
}
