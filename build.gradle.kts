plugins {
    kotlin("jvm") version "2.0.20" apply false
    kotlin("multiplatform") version "2.0.20" apply false
    kotlin("android") version "2.0.20" apply false
    kotlin("plugin.serialization") version "2.0.20" apply false
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    id("com.google.devtools.ksp") apply false
    id("io.ktor.plugin") apply false
    id("org.jetbrains.dokka")
    id("org.jetbrains.compose") version "1.7.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" apply false
    id("com.codingfeline.buildkonfig") apply false
    id("app.cash.sqldelight") apply false
    id("com.mikepenz.aboutlibraries.plugin")
    id("mooncloak.variables")
}

group = buildVariables.group
version = buildVariables.totalVersion
