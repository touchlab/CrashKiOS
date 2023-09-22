/*
 * Copyright (c) 2021 Touchlab
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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.vanniktech.maven.publish")
}

val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

kotlin {
    targetHierarchy.default()
    androidTarget {
        publishAllLibraryVariants()
    }

    macosX64()
    macosArm64()
    iosX64()
    iosArm64()
//    iosArm32()
    iosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
//    watchosX86()
    watchosX64()
    watchosDeviceArm64()
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val darwinMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.nsexceptionKt.core)
            }
        }
        val darwinTest by creating {
            dependsOn(commonTest)
        }

        val androidMain by getting {
            dependencies {
                compileOnly(libs.bugsnag.android)
            }
        }

        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().all {
            val mainCompilation = compilations.getByName("main")
            val mainSourceSet = mainCompilation.defaultSourceSet
            val testSourceSet = compilations.getByName("test").defaultSourceSet

            mainSourceSet.dependsOn(darwinMain)
            testSourceSet.dependsOn(darwinTest)

            mainCompilation.cinterops.create("bugsnag") {
                includeDirs("$projectDir/src/include")
                includeDirs("$projectDir/src/include/private")
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=", "-D_Nullable_result=_Nullable")
//            extraOpts("-mode", "sourcecode")
            }
        }
    }
}

android {
    namespace = "co.touchlab.crashkios.bugsnag"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
