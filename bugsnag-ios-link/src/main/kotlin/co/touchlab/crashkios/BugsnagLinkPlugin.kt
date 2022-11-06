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

import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import java.io.*
import java.util.*

internal val Project.kotlinExtension: KotlinMultiplatformExtension get() = extensions.getByType()

@Suppress("unused")
class BugsnagLinkPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        afterEvaluate {
            project.kotlinExtension.crashLinkerConfig(
                "-U _OBJC_CLASS_\$_Bugsnag " +
                        "-U _OBJC_CLASS_\$_BugsnagStackframe " +
                        "-U _OBJC_CLASS_\$_FIRStackFrame " +
                        "-U _OBJC_CLASS_\$_BugsnagFeatureFlag " +
                        "-U _OBJC_CLASS_\$_BugsnagError"
            )
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