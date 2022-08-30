//include(":reporter")
include(":bugsnag")
include(":crashlytics")
//include(":utils")
//include ':sample'

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.library" -> useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    val KOTLIN_VERSION: String by settings
    plugins {
        kotlin("multiplatform") version KOTLIN_VERSION
    }
}