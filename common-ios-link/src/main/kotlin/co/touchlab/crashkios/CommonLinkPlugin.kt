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

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractExecutable
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary
import org.jetbrains.kotlin.konan.target.KonanTarget
import java.io.File
import java.util.*

internal val Project.kotlinExtension: KotlinMultiplatformExtension get() = extensions.getByType()

fun KotlinMultiplatformExtension.addFrameworkLinkPath(
    frameworkFile: File,
    subpathBlock: (target: KonanTarget) -> String?
) {
    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java)
        .forEach { knt ->
            knt.binaries.filterIsInstance<NativeBinary>().forEach { binary ->
                val isExecutable = binary is AbstractExecutable
                val isDynamicFramework = binary is Framework && !binary.isStatic

                if (isExecutable || isDynamicFramework) {
                    subpathBlock(binary.target.konanTarget)?.let { subpath ->
                        binary.linkerOpts.add("-F${frameworkFile.absolutePath}/${subpath}/")
                        if (isExecutable) {
                            binary.linkerOpts.addAll(listOf("-rpath", "${frameworkFile.absolutePath}/${subpath}/"))
                        }
                    }
                }
            }
        }
}

fun findFrameworkBinaryFolder(
    execOperations: ExecOperations,
    zipUrl: String,
    frameworkName: String
): File {
    val homeDir = File(System.getProperty("user.home"))
    val touchlabDir = File(homeDir, ".touchlab")
    val outDir = File(touchlabDir, "crashkios")

    val crashFrameworkDir = File(outDir, "${frameworkName}.xcframework")

    if (crashFrameworkDir.exists()) {
        return crashFrameworkDir
    }

    outDir.mkdirs()

    val tempUuid = UUID.randomUUID().toString()
    val outfile = File(outDir, "${tempUuid}.zip")

    execOperations.exec {
        commandLine(
            "curl",
            "-L",
            zipUrl,
            "--output",
            outfile.absolutePath
        )
    }
    execOperations.exec {
        commandLine(
            "unzip",
            "${outDir.absolutePath}/${tempUuid}.zip",
            "-d",
            "${outDir.absolutePath}/$tempUuid"
        )
    }
    execOperations.exec {
        commandLine(
            "mv",
            "${outDir.absolutePath}/$tempUuid/Carthage/Build/${frameworkName}.xcframework",
            "${outDir.absolutePath}/"
        )
    }
    execOperations.exec {
        commandLine("rm", "-rdf", "${outDir.absolutePath}/$tempUuid")
    }
    execOperations.exec {
        commandLine("rm", "${outDir.absolutePath}/${tempUuid}.zip")
    }

    if (!crashFrameworkDir.exists()) {
        throw GradleException("${frameworkName} framework not found at ${crashFrameworkDir.absolutePath}")
    }

    return crashFrameworkDir
}