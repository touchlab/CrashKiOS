// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val KOTLIN_VERSION: String by project

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${KOTLIN_VERSION}")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.22.0")
    }
}

plugins {
//    kotlin("multiplatform") apply false
    id("com.android.library") version "7.2.2" apply false
    id("co.touchlab.touchlabtools.docusaurusosstemplate") version "0.1.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
