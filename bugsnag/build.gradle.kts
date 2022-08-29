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

plugins {
    kotlin("multiplatform")
}

val NSEXCEPTION_KT_VERSION: String by project
val GROUP: String by project
val VERSION_NAME: String by project

group = GROUP
version = VERSION_NAME

kotlin {
    val commonMain by sourceSets.getting
    val commonTest by sourceSets.getting
    val darwinMain by sourceSets.creating {
        dependsOn(commonMain)
    }
    val darwinTest by sourceSets.creating {
        dependsOn(commonTest)
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
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().all {
        val mainCompilation = compilations.getByName("main")
        val mainSourceSet = mainCompilation.defaultSourceSet
        val testSourceSet = compilations.getByName("test").defaultSourceSet

        mainSourceSet.dependsOn(darwinMain)
        testSourceSet.dependsOn(darwinTest)

        mainCompilation.cinterops.create("bugsnag") {
            includeDirs("$projectDir/src/include")
            compilerOpts("-DNS_FORMAT_ARGUMENT(A)=", "-D_Nullable_result=_Nullable")
//            extraOpts("-mode", "sourcecode")
        }
    }

    commonMain.dependencies {
        api(project(":reporter"))
    }

    commonTest.dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
    }

    darwinMain.dependencies {
        implementation("com.rickclephas.kmp:nsexception-kt-core:$NSEXCEPTION_KT_VERSION")
    }
}

apply(from = "../gradle/gradle-mvn-mpp-push.gradle")
