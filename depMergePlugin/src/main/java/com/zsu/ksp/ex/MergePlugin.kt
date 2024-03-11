package com.zsu.ksp.ex

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies

interface MergePluginExtension {
    val gatherClassPathFrom: SetProperty<String>
}

private val defaultCompileClasspathSet = setOf("runtimeClasspath")

class MergePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<MergePluginExtension>("ClasspathMergePlugin")
        project.afterEvaluate {
            val gatherClassPathSet = extension.gatherClassPathFrom.getOrElse(defaultCompileClasspathSet)
            val requiredAddedDependencies = hashSetOf<Any>()
            project.findDependencies(gatherClassPathSet) {
                if (it in requiredAddedDependencies) return@findDependencies
                requiredAddedDependencies += it
                project.dependencies {
                    for (requiredAddedDependency in requiredAddedDependencies) {
                        add("implementation", requiredAddedDependency)
                    }
                }
            }
        }
    }


}

private fun Project.componentId2Dependency(id: ComponentIdentifier): Any = when (id) {
    is ModuleComponentIdentifier -> "${id.group}:${id.module}:${id.version}"
    is ProjectComponentIdentifier -> project(id.projectPath)
    else -> {
        val name = id.displayName
        logger.log(LogLevel.WARN, "Meet unknown artifact($name) when trying merge dependencies.")
        name
    }
}

private fun Project.findDependencies(
    gatherClassPathSet: Set<String>,
    onDependencyFound: (Any) -> Unit
) {
    val configurations = configurations
    val requiredConfigurations = gatherClassPathSet.mapNotNull { configurations.findByName(it) }
    for (configuration in requiredConfigurations) {
        for (dependency in configuration.incoming.dependencies) {
            if (dependency is ProjectDependency) {
                val childProject = dependency.dependencyProject
                childProject.findChildDependencies(gatherClassPathSet, onDependencyFound)
            }
            onDependencyFound(dependency)
        }
    }
}

private fun Project.findChildDependencies(
    gatherClassPathSet: Set<String>,
    onDependencyFound: (Any) -> Unit
) = afterEvaluate {
    val configurations = configurations
    val requiredConfigurations = gatherClassPathSet.mapNotNull { configurations.findByName(it) }
    for (configuration in requiredConfigurations) {
        val artifacts = configuration.resolvedConfiguration.resolvedArtifacts
        artifacts.map { it.id.componentIdentifier }
            .map { componentId2Dependency(it) }
            .forEach(onDependencyFound)
    }
}

