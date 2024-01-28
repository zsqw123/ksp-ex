# KSP-EX

**Note: still in the experimental stage and is not stable for production, It may be changed at any time**

Extension for KSP, currently extensions:

- `allDeclarationsWithDependencies()` get all kotlin declarations(`KSDeclaration`) in itself and dependencies

## How to use

Using it by one line call:

```kotlin
import zsu.ksp.ex.ext

// use it!
resolver.ext
    .allDeclarationsWithDependencies()
```

### Dependencies
[Maven Central: ksp-ext](https://central.sonatype.com/artifact/host.bytedance/ksp-ext)

Latest version: ![Maven Central](https://img.shields.io/maven-central/v/host.bytedance/ksp-ext)

Adds following maven artifact into your ksp plugin dependencies:

```kotlin
dependencies {
    implementation("host.bytedance:ksp-ext:<latest>")
}
```

adds gradle dependencies

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
