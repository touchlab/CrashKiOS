include(":core")
include(":bugsnag")
include(":bugsnag-ios-link")
include(":crashlytics")
include(":crashlytics-ios-link")

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
