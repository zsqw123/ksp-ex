pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ksp-ex"
includeBuild("insidePlugin")

include(":app")

include(":ksp-ex")
include(":demo")
include(":demo:ksp")
include(":demo:lib")
include(":demo:lib2")
include(":demo:api")
