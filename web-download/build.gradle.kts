import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.*
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    applyDefaultHierarchyTemplate()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                enabled = false
            }
        }

        binaries.executable()
    }

    explicitApi()

    sourceSets {
        all {
            // Disable warnings and errors related to these expected @OptIn annotations.
            // See: https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            languageSettings.optIn("-Xexpect-actual-classes")
            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        }

        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/commonMain/kotlin")

            dependencies {
                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.core)

                // Declarative UI - Compose Multiplatform
                api(compose.runtime)
                api(compose.foundation)
                api(compose.materialIconsExtended)

                // Animations
                api(compose.animation)
                api(compose.animationGraphics)

                api(compose.material3)

                // Resources
                implementation(compose.components.resources)

                // UI Shaders - HypnoticCanvas
                // https://github.com/mikepenz/HypnoticCanvas
                // Apache 2.0: https://github.com/mikepenz/HypnoticCanvas/blob/dev/LICENSE
                implementation("com.mikepenz.hypnoticcanvas:hypnoticcanvas:_")

                // Async Image Loading - coil
                // https://github.com/coil-kt/coil
                api("io.coil-kt.coil3:coil:_")
                api("io.coil-kt.coil3:coil-network-ktor3:_")
                implementation("io.coil-kt.coil3:coil-compose:_")

                // konstruct - Dependency Injection
                // https://github.com/mooncloak/konstruct
                api("com.mooncloak.kodetools.konstruct:konstruct-runtime:_")
            }
        }
    }
}

dependencies {
    // Annotation Processors:

    // konstruct - Dependency Injection
    // https://github.com/mooncloak/konstruct
    add("kspCommonMainMetadata", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
    add("kspWasmJs", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.JVM_11
}

// Don't run npm install scripts, protects against
// https://blog.jetbrains.com/kotlin/2021/10/important-ua-parser-js-exploit-and-kotlin-js/ etc.
tasks.withType<KotlinNpmInstallTask> {
    args += "--ignore-scripts"
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mooncloak.vpn.web.download.resource"
    generateResClass = always
}

compose.experimental {
    @Suppress("DEPRECATION")
    web.application {}
}

// `./gradlew :web-download:generateBuildKonfig`
buildkonfig {
    packageName = "com.mooncloak.vpn.web.download.info"
    objectName = "WebBuildConfig"

    defaultConfigs {
        buildConfigField(
            type = STRING,
            name = "appVersion",
            value = buildVariables.version
        )
        buildConfigField(
            type = STRING,
            name = "playStoreUrl",
            value = buildVariables.playStoreUrl,
            nullable = true
        )
        buildConfigField(
            type = STRING,
            name = "playStorePreRegisterUrl",
            value = buildVariables.playStorePreRegisterUrl,
            nullable = true
        )
        buildConfigField(
            type = STRING,
            name = "directDownloadUrl",
            value = buildVariables.directDownloadUrl,
            nullable = true
        )
    }
}
