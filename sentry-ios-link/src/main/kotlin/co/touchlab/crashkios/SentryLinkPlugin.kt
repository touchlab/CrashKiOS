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

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import org.apache.commons.io.IOUtils
import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.*
import java.util.UUID

internal val Project.kotlinExtension: KotlinMultiplatformExtension get() = extensions.getByType()

@Suppress("unused")
class SentryLinkPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        if (HostManager.hostIsMac) {
            afterEvaluate {
                val frameworkFile = downloadZip(
                    zipUrl = "https://github.com/getsentry/sentry-cocoa/releases/download/8.21.0/Sentry.xcframework.zip",
                    frameworkName = "Sentry"
                )
                project.kotlinExtension.addFrameworkLinkPath(
                    frameworkFile = frameworkFile,
                    subpathBlock = ::findXcframeworkSubfolder
                )
            }
        }
    }
}

//private fun KotlinMultiplatformExtension.addFrameworkLinkPath(frameworkFile: File) {
//    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java)
//        .forEach { knt ->
//            knt.binaries.filterIsInstance<NativeBinary>().filter { binary ->
//                (binary is Framework && !binary.isStatic) || (binary is AbstractExecutable)
//            }.forEach { binary ->
//                val subpathPath = findXcframeworkSubfolder(binary.target.konanTarget)
//                println("KonanTarget: ${binary.target.konanTarget}")
//                println("subpathPath: $subpathPath")
//
//                subpathPath?.let { subpath ->
//                    binary.linkerOpts.add("-F${frameworkFile.absolutePath}/${subpath}/")
//                }
//            }
//        }
//}

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

//private fun Project.downloadZip():File {
//    val homeDir = File(System.getProperty("user.home"))
//    val outDir = File(homeDir, ".touchlab")
//
//    val sentryFrameworkDir = File(outDir, "Sentry.xcframework")
//    if (sentryFrameworkDir.exists()) {
//        return sentryFrameworkDir
//    }
//
//    val tempUuid = UUID.randomUUID().toString()
//    val client = OkHttpClient.Builder().followRedirects(true).build()
//
//    val request: Request = Builder()
//        .url("https://github.com/getsentry/sentry-cocoa/releases/download/8.21.0/Sentry.xcframework.zip")
//        .build()
//
//    val response = client.newCall(request).execute()
//    response.body?.byteStream()?.let { inp ->
//        outDir.mkdirs()
//        val outfile = File(outDir, "${tempUuid}.zip")
//        val outStream = BufferedOutputStream(FileOutputStream(outfile))
//        IOUtils.copy(inp, outStream)
//        outStream.close()
//    }
//
//    procRunFailLog(
//        "unzip",
//        "${outDir.absolutePath}/${tempUuid}.zip",
//        "-d",
//        "${outDir.absolutePath}/$tempUuid"
//    )
//
//    procRunFailLog(
//        "mv",
//        "${outDir.absolutePath}/$tempUuid/Carthage/Build/Sentry.xcframework",
//        "${outDir.absolutePath}/"
//    )
//
//    procRunFailLog("rm", "-rdf", "${outDir.absolutePath}/$tempUuid")
//    procRunFailLog("rm", "${outDir.absolutePath}/${tempUuid}.zip")
//
//    if(!sentryFrameworkDir.exists()){
//        throw GradleException("Sentry framework not found at ${sentryFrameworkDir.absolutePath}")
//    }
//
//    return sentryFrameworkDir
//}
//
///**
// * Run a process. If it fails, write output to gradle error log and throw exception.
// */
//internal fun Project.procRunFailLog(vararg params: String): List<String> {
//    val output = mutableListOf<String>()
//    try {
//        logger.info("Project.procRunFailLog: ${params.joinToString(" ")}")
//        procRun(*params) { line, _ -> output.add(line) }
//    } catch (e: Exception) {
//        output.forEach { logger.error("error: $it") }
//        throw e
//    }
//    return output
//}
//
//internal fun procRun(vararg params: String, processLines: (String, Int) -> Unit) {
//    val process = ProcessBuilder(*params)
//        .redirectErrorStream(true)
//        .start()
//
//    val streamReader = InputStreamReader(process.inputStream)
//    val bufferedReader = BufferedReader(streamReader)
//    var lineCount = 1
//
//    bufferedReader.forEachLine { line ->
//        processLines(line, lineCount)
//        lineCount++
//    }
//
//    bufferedReader.close()
//    val returnValue = process.waitFor()
//    if (returnValue != 0)
//        throw GradleException("Process failed: ${params.joinToString(" ")}")
//}