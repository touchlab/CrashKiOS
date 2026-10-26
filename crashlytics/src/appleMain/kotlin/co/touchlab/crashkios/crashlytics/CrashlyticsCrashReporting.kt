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
 * `enableCrashlytics()` from shared startup code is safe before or after this call; events
 * sent before a sink is configured are dropped.
 */
public class CrashlyticsCrashReporting(private val sink: CrashKiOSCrashlyticsSinkProtocol) : CrashReportingImplementation {
    override fun install() {
        crashlyticsRegistry.register(sink) { throwable ->
            CrashlyticsKotlin.sendFatalException(throwable)
        }
        CrashlyticsKotlin.implementation = CrashlyticsCallsActual()
    }
}
