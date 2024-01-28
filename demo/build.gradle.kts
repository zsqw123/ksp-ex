plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

dependencies {
    ksp(project(":demo:ksp"))
    implementation(project(":demo:lib"))
    implementation(project(":demo:api"))
}
