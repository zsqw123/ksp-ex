plugins {
    kotlin("jvm") version "1.9.22"
    `kotlin-dsl`
    id("inside")
    id("java-gradle-plugin")
}

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
    plugins {
        create("depsMerge") {
            id = "ksp-deps-merge"
            implementationClass = "com.zsu.ksp.ex.MergePlugin"
        }
    }
}
