import com.mooncloak.kodetools.kenv.Kenv
import com.mooncloak.kodetools.kenv.dotenv
import com.mooncloak.kodetools.kenv.properties
import com.mooncloak.kodetools.kenv.store.getBooleanOrNull
import com.mooncloak.kodetools.kenv.store.getStringOrNull
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class BuildVariablesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        projectBuildVariables[target.name] = BuildVariables(
            kenv = Kenv {
                system()

                properties(file = target.rootProject.layout.projectDirectory.file("app.properties").asFile)

                val envFile = target.rootProject.layout.projectDirectory.file(".env").asFile

                if (envFile.exists()) {
                    dotenv(file = envFile)
                }
            }
        )
    }
}

class BuildVariables internal constructor(
    private val kenv: Kenv
) {

    val group: String
        get() = kenv["group"].value

    val idPrefix: String
        get() = "com.mooncloak.app"

    val totalVersion: String
        get() = "$versionCode-$version"

    val version: String
        get() = kenv["version"].value

    val appName: String
        get() = kenv["appName"].value

    val appDescription: String
        get() = kenv["appDescription"].value

    /**
     * The Msi binary package version is very strict on what can be used as a version. They only support
     * MAJOR.MINOR.BUILD with numbers.
     */
    val strictVersionWithoutSuffix: String
        get() {
            val index = version.indexOfFirst { char -> !char.isDigit() && char != '.' }

            return if (index == -1) {
                version
            } else {
                version.substring(startIndex = 0, endIndex = index)
            }
        }

    val versionCode: Int
        get() = getCommitCount()

    val environment: String
        get() = runCatching { kenv.getStringOrNull("environment") }.getOrNull()
            ?: "production"

    val cdnPublishAccessKey: String?
        get() = runCatching { kenv.getStringOrNull("mooncloakCdnAccessKey") }.getOrNull()

    val cdnPublishSecretKey: String?
        get() = runCatching { kenv.getStringOrNull("mooncloakCdnSecretKey") }.getOrNull()

    val publishAsLatest: Boolean
        get() = runCatching { kenv.getBooleanOrNull("publishAsLatest") }.getOrNull() ?: true

    val playStoreUrl: String?
        get() = runCatching { kenv.getStringOrNull("playStoreUrl") }.getOrNull()

    val playStorePreRegisterUrl: String?
        get() = runCatching { kenv.getStringOrNull("playStorePreRegisterUrl") }.getOrNull()

    val directDownloadUrl: String?
        get() = runCatching { kenv.getStringOrNull("directDownloadUrl") }.getOrNull()
}

val Project.buildVariables: BuildVariables
    get() {
        var variables = projectBuildVariables[this.name]

        if (variables == null) {
            this.logger.warn("The '${BuildVariablesPlugin::class.simpleName}' was not applied to project with name '${this.name}'. Attempting to load root project build variables.")
        }

        if (this != this.rootProject) {
            variables = projectBuildVariables[this.rootProject.name]
        }

        return variables
            ?: error("Failed to load required build variables. Make sure the '${BuildVariablesPlugin::class.simpleName}' is applied to the project.")
    }

private val projectBuildVariables = mutableMapOf<String, BuildVariables>()
