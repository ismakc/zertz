import org.gradle.jvm.tasks.Jar
import org.gradle.api.plugins.ExtensionAware

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

buildscript {
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.3")
    }
}

repositories {
    jcenter()
}

plugins {
    application
    kotlin("jvm") version ("1.2.20")
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

application {
    mainClassName = "MainKt"
    group = "es.ismakc.team"
    version = "1.0.0-SNAPSHOT"
    applicationName = "zertz"
}

extensions.getByType(JUnitPlatformExtension::class.java).apply {
    filters {
        engines {
            include("spek")
        }
    }
}

val spekVersion = "1.1.5"

dependencies {
    compile(kotlin("stdlib", kotlinVersion))

    testCompile(kotlin("reflect", kotlinVersion))
    testCompile(kotlin("test", kotlinVersion))
    testCompile("org.jetbrains.spek:spek-api:$spekVersion", { exclude(group = "org.jetbrains.kotlin") })

    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion", {
        exclude(group = "'org.junit.platform'")
        exclude(group = "'org.jetbrains.kotlin'")
    })
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
    configurations["compileClasspath"].forEach { file: File -> from(zipTree(file.absoluteFile)) }
}

// extension for configuration
fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
    when (this) {
        is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
        else -> throw Exception("${this::class} must be an instance of ExtensionAware")
    }
}
