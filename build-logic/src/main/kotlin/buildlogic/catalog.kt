package buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog


import org.gradle.api.GradleException
import org.gradle.api.artifacts.MinimalExternalModuleDependency

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency
import kotlin.jvm.optionals.getOrElse

val Project.catalog: VersionCatalog
    get() = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.lib(libraryName: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(libraryName)
        .getOrElse {
            throw GradleException(
                "No library named [$libraryName] found in catalog: ${this.name}"
            )
        }

fun VersionCatalog.alias(name: String): Provider<PluginDependency> = findPlugin(name).get()

fun VersionCatalog.version(name: String) = findVersion(name).get()