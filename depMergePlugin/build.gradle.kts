plugins {
    kotlin("jvm") version "1.9.22"
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("insidePublish")
}

group = "host.bytedance"

val exVersion: String by project

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin"))
}

gradlePlugin {
    website = "https://github.com/zsqw123/ksp-ex"
    vcsUrl = "https://github.com/zsqw123/ksp-ex"
    plugins {
        create("depsMerge") {
            id = "host.bytedance.ksp-deps-merge"
            displayName = "Plugin for integrates all of child dependencies into single project "
            description = "Plugin for integrates all of child dependencies into single project"
            tags = listOf("ksp", "kotlin")
            implementationClass = "com.zsu.ksp.ex.MergePlugin"
        }
    }
}
