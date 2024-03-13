/*
 * Copyright (c) 2022 Touchlab.
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

package co.touchlab.crashkios

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.of
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget

internal val Project.kotlinExtension: KotlinMultiplatformExtension get() = extensions.getByType()

@Suppress("unused")
class CrashlyticsLinkPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        if (HostManager.hostIsMac) {
            afterEvaluate {
                val sentryFrameworkProvider = providers.of(CrashlyticsFrameworkValueSource::class) {}
                val sentryFramework = sentryFrameworkProvider.get()

                project.kotlinExtension.addFrameworkLinkPath(
                    frameworkFile = sentryFramework,
                    linkerName = "FirebaseCrashlytics",
                    subpathBlock = ::findXcframeworkSubfolder
                )
            }
        }
    }
}

private fun findXcframeworkSubfolder(target: KonanTarget): String? = when (target) {
    KonanTarget.IOS_ARM64 -> "ios-arm64"
    KonanTarget.IOS_SIMULATOR_ARM64,
    KonanTarget.IOS_X64 -> "ios-arm64_x86_64-simulator"

    KonanTarget.MACOS_ARM64,
    KonanTarget.MACOS_X64 -> "macos-arm64_x86_64"

    KonanTarget.TVOS_ARM64 -> "tvos-arm64"
    KonanTarget.TVOS_SIMULATOR_ARM64,
    KonanTarget.TVOS_X64 -> "tvos-arm64_x86_64-simulator"

    KonanTarget.WATCHOS_ARM32,
    KonanTarget.WATCHOS_ARM64,
    KonanTarget.WATCHOS_DEVICE_ARM64 -> "watchos-arm64_arm64_32_armv7k"

    KonanTarget.WATCHOS_SIMULATOR_ARM64,
    KonanTarget.WATCHOS_X64 -> "watchos-arm64_i386_x86_64-simulator"

    KonanTarget.IOS_ARM32,
    KonanTarget.WATCHOS_X86,

    KonanTarget.ANDROID_ARM32,
    KonanTarget.ANDROID_ARM64,
    KonanTarget.ANDROID_X64,
    KonanTarget.ANDROID_X86,
    KonanTarget.LINUX_ARM32_HFP,
    KonanTarget.LINUX_ARM64,
    KonanTarget.LINUX_MIPS32,
    KonanTarget.LINUX_MIPSEL32,
    KonanTarget.LINUX_X64,
    KonanTarget.MINGW_X64,
    KonanTarget.MINGW_X86,
    KonanTarget.WASM32,
    is KonanTarget.ZEPHYR -> null
}