import java.util.*

plugins {
    kotlin("jvm") version "1.9.22"
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("inside")
}

group = "host.bytedance"
val rootProperties = Properties()

File(projectDir.parentFile, "gradle.properties").reader().use {
    rootProperties.load(it)
}

val exVersion: String? by rootProperties
exVersion?.let { version = it }

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
