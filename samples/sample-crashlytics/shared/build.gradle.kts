/*
 * Copyright (c) 2021 Touchlab
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("co.touchlab.crashkios.crashlyticslink")
}

android {
    namespace = "co.touchlab.crashkiossample"
    compileSdk = projectLibs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = projectLibs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

version = "0.1.2"

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    // Note: iosSimulatorArm64 target requires that all dependencies have M1 support
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api("co.touchlab.crashkios:crashlytics")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))

            }
        }
    }

    cocoapods {
        summary = "Sample for CrashKiOS"
        homepage = "https://www.touchlab.co"
        ios.deploymentTarget = "14.1"
        framework {
            isStatic = false
        }
    }
}
