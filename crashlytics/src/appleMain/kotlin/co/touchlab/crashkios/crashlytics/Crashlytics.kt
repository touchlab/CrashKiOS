@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.crashlytics

import co.touchlab.crashkios.core.CrashReportingImplementation
import co.touchlab.crashkios.crashlytics.objc.CrashKiOSCrashlyticsSinkProtocol
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * The Crashlytics backend for [co.touchlab.crashkios.core.CrashKiOS.configure]. Wraps
 * the Swift-implemented [sink] that forwards CrashKiOS events to Firebase Crashlytics.
 *
 * Call once, from Swift, at startup — after `FirebaseApp.configure()` and before any
 * Kotlin code runs:
 * ```swift
 * FirebaseApp.configure()
 * CrashKiOS.shared.configure(crashReporting: CrashlyticsCrashReporting(sink: CrashlyticsSink()))
 * ```
 * `CrashlyticsSink` ships in the `CrashKiOSCrashlytics` Swift package (this repo's
 * `Package.swift`), or copy it from the docs. Add `export("co.touchlab.crashkios:crashlytics")`
 * AND `export("co.touchlab.crashkios:core")` to your framework configuration for
 * clean unmangled names.
 *
 * Calling `enableCrashlytics()` (or constructing `CrashlyticsCallsActual`) on an Apple
 * target WITHOUT having configured a sink fails fast with a descriptive error rather
 * than silently dropping crash reports.
 */
public class CrashlyticsCrashReporting(private val sink: CrashKiOSCrashlyticsSinkProtocol) : CrashReportingImplementation {
    override fun install() {
        crashlyticsRegistry.register(sink) { throwable ->
            CrashlyticsKotlin.sendFatalException(throwable)
        }
        CrashlyticsKotlin.implementation = CrashlyticsCallsActual()
    }
}

/**
 * The unhandled exception hook is now installed by [CrashlyticsCrashReporting.install];
 * this function does nothing.
 */
@Deprecated(
    "CrashKiOS.configure(CrashlyticsCrashReporting(...)) installs the unhandled-exception " +
        "hook; remove this call.",
)
public fun setCrashlyticsUnhandledExceptionHook() {
}
