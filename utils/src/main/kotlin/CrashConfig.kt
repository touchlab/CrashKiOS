package co.touchlab.faktory

import org.jetbrains.kotlin.gradle.plugin.mpp.Framework


fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.sentryLinkerConfig() {
    crashLinkerConfig(
        "-U _OBJC_CLASS_\$_SentryException " +
                "-U _OBJC_CLASS_\$_SentryDependencyContainer " +
                "-U _OBJC_CLASS_\$_SentryEvent " +
                "-U _OBJC_CLASS_\$_SentryEnvelopeItem " +
                "-U _OBJC_CLASS_\$_SentrySDK " +
                "-U _OBJC_CLASS_\$_SentryEnvelopeHeader " +
                "-U _OBJC_CLASS_\$_SentryEnvelope " +
                "-U _sentrycrash_async_backtrace_decref " +
                "-U _OBJC_CLASS_\$_SentryMechanism " +
                "-U _sentrycrashsc_initWithBacktrace"
    )
}

fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.crashlyticsLinkerConfig() {
    crashLinkerConfig(
        "-U _FIRCLSExceptionRecordNSException " +
                "-U _OBJC_CLASS_\$_FIRStackFrame " +
                "-U _OBJC_CLASS_\$_FIRExceptionModel " +
                "-U _OBJC_CLASS_\$_FIRCrashlytics "
    )
}

fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.bugsnagLinkerConfig() {
    crashLinkerConfig(
        "-U _OBJC_CLASS_\$_Bugsnag " +
                "-U _OBJC_CLASS_\$_BugsnagStackframe " +
                "-U _OBJC_CLASS_\$_FIRStackFrame " +
                "-U _OBJC_CLASS_\$_BugsnagFeatureFlag " +
                "-U _OBJC_CLASS_\$_BugsnagError "
    )
}

private fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.crashLinkerConfig(linkerOpts: String) {
    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).map { target ->
        target.compilations.findByName("main")
    }
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