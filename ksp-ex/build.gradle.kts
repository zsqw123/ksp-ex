plugins {
    kotlin("jvm")
    id("insidePublish")
}

dependencies {
    implementation(D.ksp)
    compileOnly(D.kspImpl)
    compileOnly(D.compilerEmbeddable)
}

tasks.test {
    useJUnitPlatform()
}
