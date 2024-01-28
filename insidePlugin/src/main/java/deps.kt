import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@Suppress("ConstPropertyName")
object D {
    const val KT = "1.9.22"
    const val compilerEmbeddable = "org.jetbrains.kotlin:kotlin-compiler-embeddable:$KT";

    const val KSP_VERSION = "1.9.22-1.0.17"
    const val ksp = "com.google.devtools.ksp:symbol-processing-api:$KSP_VERSION"
    const val kspImpl = "com.google.devtools.ksp:symbol-processing:$KSP_VERSION"

    const val junitBom = "org.junit:junit-bom:5.9.1"
    const val junitJupiter = "org.junit.jupiter:junit-jupiter"
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"
}

fun DependencyHandler.jvmTestDeps(configurationName: String = "testImplementation") {
    add(configurationName, platform(D.junitBom))
    add(configurationName, D.junitJupiter)
}

/**
 * @param dependencyPath maven artifact full-id or project path start with `:`
 */
private fun DependencyHandler.addDeps(configurationName: String, dependencyPath: String) {
    if (dependencyPath.startsWith(":")) {
        add(configurationName, project(mapOf("path" to dependencyPath)))
    } else {
        add(configurationName, dependencyPath)
    }
}
