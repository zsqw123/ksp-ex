import com.zsu.ksp.ex.MergePluginExtension

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("host.bytedance.ksp-deps-merge")
}

dependencies {
    ksp(project(":demo:ksp"))
    implementation(project(":demo:lib"))
    implementation(project(":demo:api"))
}

configure<MergePluginExtension> {
    gatherClassPathFrom = setOf("runtimeClasspath")
}
