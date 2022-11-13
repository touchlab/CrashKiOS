include(":core")
//include(":bugsnag")
//include(":bugsnag-ios-link")
//include(":crashlytics")
//include(":crashlytics-ios-link")

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
        mavenLocal()
        mavenCentral()
    }
    val KOTLIN_VERSION: String by settings
    plugins {
        kotlin("multiplatform") version KOTLIN_VERSION
    }
}