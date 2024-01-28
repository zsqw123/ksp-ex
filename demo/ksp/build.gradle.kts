plugins {
    kotlin("jvm")
}

dependencies {
    implementation(D.ksp)
    implementation(project(":ksp-ex"))
    implementation(project(":demo:api"))
}
