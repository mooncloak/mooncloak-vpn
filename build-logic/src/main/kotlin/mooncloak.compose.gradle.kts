import org.gradle.api.JavaVersion
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

plugins {
    kotlin("multiplatform")
}

kotlin {
    applyDefaultHierarchyTemplate()

    js {
        browser {
            testTask {
                enabled = false
            }
        }

        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                enabled = false
            }
        }

        binaries.executable()
    }

    // TODO: Re-enable when dependencies supports: linuxArm64()
    // TODO: Re-enable when dependencies supports: linuxX64()

    // TODO: Re-enable when dependencies supports: mingwX64()

    // TODO: Re-enable when dependencies supports: macosX64()
    // TODO: Re-enable when dependencies supports: macosArm64()

    // TODO: Re-enable: iosArm64()
    // TODO: Re-enable: iosX64()
    // TODO: Re-enable: iosSimulatorArm64()

    // TODO: Re-enable when dependencies supports: tvosArm64()
    // TODO: Re-enable when dependencies supports: tvosX64()
    // TODO: Re-enable when dependencies supports: tvosSimulatorArm64()

    // TODO: Re-enable when dependencies supports: watchosArm32()
    // TODO: Re-enable when dependencies supports: watchosArm64()
    // TODO: Re-enable when dependencies supports: watchosX64()
    // TODO: Re-enable when dependencies supports: watchosSimulatorArm64()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    explicitApi()

    // Ensure xml test reports are generated
    tasks.named("jvmTest", Test::class).configure {
        reports.junitXml.required.set(true)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.JVM_11
}

tasks.withType<KotlinTest> {
    if (targetName == "tvosSimulatorArm64" || targetName == "watchosSimulatorArm64") {
        enabled = false
    }
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
