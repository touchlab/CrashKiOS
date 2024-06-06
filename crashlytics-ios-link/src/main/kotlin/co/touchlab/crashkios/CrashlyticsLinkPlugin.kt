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
import org.jetbrains.kotlin.gradle.dsl.KotlinArtifactsExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeFrameworkConfig
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.targets.native.tasks.artifact.kotlinArtifactsExtension

internal val Project.kotlinExtension: KotlinMultiplatformExtension get() = extensions.getByType()

@Suppress("unused")
class CrashlyticsLinkPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val linkerArgs = "-U _FIRCLSExceptionRecordNSException " +
                "-U _OBJC_CLASS_\$_FIRStackFrame " +
                "-U _OBJC_CLASS_\$_FIRExceptionModel " +
                "-U _OBJC_CLASS_\$_FIRCrashlytics"
        afterEvaluate {
            project.kotlinExtension.crashLinkerConfig(linkerArgs)
            project.kotlinArtifactsExtension.crashLinkerConfigArtifacts(linkerArgs)
        }
    }
}

private fun KotlinArtifactsExtension.crashLinkerConfigArtifacts(linkerOpts: String) {
    artifactConfigs.withType(KotlinNativeFrameworkConfig::class.java).configureEach {
        if (!isStatic) {
            toolOptions {
                freeCompilerArgs.add("-linker-options")
                freeCompilerArgs.add(linkerOpts)
            }
        }
    }
}

private fun KotlinMultiplatformExtension.crashLinkerConfig(linkerOpts: String) {
    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java)
        .map { target ->
            val mainCompilation = target.compilations.getByName("main")
            val dynamicFrameworks =
                target.binaries.filterIsInstance<Framework>().filter { framework -> !framework.isStatic }

            Pair(mainCompilation, dynamicFrameworks)
        }
        .forEach { pair ->
            if (!pair.second.isEmpty()) {
                pair.first.kotlinOptions.freeCompilerArgs += listOf("-linker-options", linkerOpts)
            }
        }
}