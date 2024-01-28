plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
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
