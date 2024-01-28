plugins {
    kotlin("jvm")
}

dependencies {
    implementation(D.ksp)
    implementation(project(":ksp-ex"))
    // online dependency
//    implementation("host.bytedance:ksp-ex:0.0.1-beta")
    implementation(project(":demo:api"))
}
