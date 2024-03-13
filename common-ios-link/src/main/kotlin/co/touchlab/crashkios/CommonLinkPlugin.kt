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
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.*
import java.util.UUID

internal val Project.kotlinExtension: KotlinMultiplatformExtension get() = extensions.getByType()

fun KotlinMultiplatformExtension.addFrameworkLinkPath(frameworkFile: File, subpathBlock:(target: KonanTarget)->String?) {
    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java)
        .forEach { knt ->
            knt.binaries.filterIsInstance<NativeBinary>().filter { binary ->
                (binary is Framework && !binary.isStatic) || (binary is AbstractExecutable)
            }.forEach { binary ->
                val subpathPath = subpathBlock(binary.target.konanTarget)
                println("KonanTarget: ${binary.target.konanTarget}")
                println("subpathPath: $subpathPath")

                subpathPath?.let { subpath ->
                    binary.linkerOpts.add("-F${frameworkFile.absolutePath}/${subpath}/")
                }
            }
        }
}

fun Project.downloadZip(zipUrl:String, frameworkName:String):File {
    val homeDir = File(System.getProperty("user.home"))
    val outDir = File(homeDir, ".touchlab")

    val crashFrameworkDir = File(outDir, "${frameworkName}.xcframework")
    if (crashFrameworkDir.exists()) {
        return crashFrameworkDir
    }

    val tempUuid = UUID.randomUUID().toString()
    val client = OkHttpClient.Builder().followRedirects(true).build()

    val request: Request = Builder()
        .url(zipUrl)
        .build()

    val response = client.newCall(request).execute()
    response.body?.byteStream()?.let { inp ->
        outDir.mkdirs()
        val outfile = File(outDir, "${tempUuid}.zip")
        val outStream = BufferedOutputStream(FileOutputStream(outfile))
        IOUtils.copy(inp, outStream)
        outStream.close()
    }

    procRunFailLog(
        "unzip",
        "${outDir.absolutePath}/${tempUuid}.zip",
        "-d",
        "${outDir.absolutePath}/$tempUuid"
    )

    procRunFailLog(
        "mv",
        "${outDir.absolutePath}/$tempUuid/Carthage/Build/${frameworkName}.xcframework",
        "${outDir.absolutePath}/"
    )

    procRunFailLog("rm", "-rdf", "${outDir.absolutePath}/$tempUuid")
    procRunFailLog("rm", "${outDir.absolutePath}/${tempUuid}.zip")

    if(!crashFrameworkDir.exists()){
        throw GradleException("${frameworkName} framework not found at ${crashFrameworkDir.absolutePath}")
    }

    return crashFrameworkDir
}

/**
 * Run a process. If it fails, write output to gradle error log and throw exception.
 */
internal fun Project.procRunFailLog(vararg params: String): List<String> {
    val output = mutableListOf<String>()
    try {
        logger.info("Project.procRunFailLog: ${params.joinToString(" ")}")
        procRun(*params) { line, _ -> output.add(line) }
    } catch (e: Exception) {
        output.forEach { logger.error("error: $it") }
        throw e
    }
    return output
}

internal fun procRun(vararg params: String, processLines: (String, Int) -> Unit) {
    val process = ProcessBuilder(*params)
        .redirectErrorStream(true)
        .start()

    val streamReader = InputStreamReader(process.inputStream)
    val bufferedReader = BufferedReader(streamReader)
    var lineCount = 1

    bufferedReader.forEachLine { line ->
        processLines(line, lineCount)
        lineCount++
    }

    bufferedReader.close()
    val returnValue = process.waitFor()
    if (returnValue != 0)
        throw GradleException("Process failed: ${params.joinToString(" ")}")
}