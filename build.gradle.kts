// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val KOTLIN_VERSION: String by project

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${KOTLIN_VERSION}")
    }
}

plugins {
//    kotlin("multiplatform") apply false
    id("com.android.library") version "4.1.2" apply false
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
