plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.touchlab.docusaurus.template)
    alias(libs.plugins.gradle.publish) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}