plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation(project(":demo:api"))
    implementation(project(":demo:lib2"))
}
