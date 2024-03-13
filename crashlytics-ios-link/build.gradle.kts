/*
 * Copyright (c) 2022 Touchlab.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
plugins {
    `kotlin-dsl`
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish.base")
    id("com.gradle.plugin-publish")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    website.set("https://github.com/touchlab/CrashKiOS")
    vcsUrl.set("https://github.com/touchlab/CrashKiOS.git")

    description = "CrashKiOS linker params for Crashlytics on iOS"
    plugins {
        register("crashlytics-ios-link-plugin") {
            id = "co.touchlab.crashkios.crashlyticslink"
            implementationClass = "co.touchlab.crashkios.CrashlyticsLinkPlugin"
            displayName = "Crashlytics iOS Link Plugin"
            tags.set(listOf("kmm", "kotlin", "multiplatform", "mobile", "ios"))
        }
    }
}

dependencies {
    implementation(gradleApi())
    compileOnly(kotlin("gradle-plugin"))
    implementation(project(":common-ios-link"))
}

kotlin {
    jvmToolchain(11)
}

val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

mavenPublishing {
    publishToMavenCentral()
    val releaseSigningEnabled =
        project.properties["RELEASE_SIGNING_ENABLED"]?.toString()?.equals("false", ignoreCase = true) != true
    if (releaseSigningEnabled) signAllPublications()
    pomFromGradleProperties()
}