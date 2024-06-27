/*
 * Copyright (c) 2021 Touchlab
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

include(":app", ":shared")

dependencyResolutionManagement {
    versionCatalogs {
        create("projectLibs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

includeBuild("../..") {
    dependencySubstitution {
        substitute(module("co.touchlab.crashkios:crashlytics"))
            .using(project(":crashlytics")).because("we want to auto-wire up sample dependency")
        substitute(module("co.touchlab.crashkios.crashlyticslink:co.touchlab.crashkios.crashlyticslink.gradle.plugin"))
            .using(project(":crashlytics-ios-link")).because("we want to auto-wire up sample dependency")
    }
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}
