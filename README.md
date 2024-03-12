# KSP-EX

**Note: still in the experimental stage and is not stable for production, It may be changed at any time**

Extensions for KSP, extensions currently:

- `allDeclarationsWithDependencies()` get all kotlin declarations(`KSDeclaration`) in itself and dependencies

## How to use

Using it by one line call: `resolver.ext`

```kotlin
import zsu.ksp.ex.ext

override fun process(resolver: Resolver): List<KSAnnotated> {
    // use it by extension `ext` of [com.google.devtools.ksp.processing.Resolver]
    val allDeclarations = resolver.ext
        .allDeclarationsWithDependencies()
    // process your annotations here
}
```

### Dependencies

Latest
version: [![Maven Central](https://img.shields.io/maven-central/v/host.bytedance/ksp-ex)](https://central.sonatype.com/artifact/host.bytedance/ksp-ex)

Adds following maven artifact into your ksp plugin's gradle dependencies:

```kotlin
dependencies {
    implementation("host.bytedance:ksp-ex:<latest>")
}
```

recommended version alignments:

| Kotlin | KSP           | KSP-EX     |
|--------|---------------|------------|
| 1.9.22 | 1.9.22-1.0.17 | 0.0.1-beta |

### Dependency aggregation

KSP will analyze the current module and its dependencies, but not the deeper submodules that the dependent module
depends on. If you need to analyze all the modules, you can aggregate dependencies by apply the following gradle plugin
on your module which wants to aggregates all dependencies.

```kotlin
// Using the plugins DSL
plugins {
    id("host.bytedance.ksp-deps-merge") version "<latest>"
}

// or using legacy plugin application
buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("host.bytedance:ksp-deps-merge:<latest>")
    }
}

apply(plugin = "host.bytedance.ksp-deps-merge")
```

The plugin will be published both **gradle plugin portal** and **maven central**.

- maven central: [![Maven Central](https://img.shields.io/maven-central/v/host.bytedance/ksp-deps-merge)](https://central.sonatype.com/artifact/host.bytedance/ksp-deps-merge)
- gradle plugin portal: [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/host.bytedance.ksp-deps-merge)](https://plugins.gradle.org/plugin/host.bytedance.ksp-deps-merge)


### License

```
Copyright 2024 zsqw123

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
