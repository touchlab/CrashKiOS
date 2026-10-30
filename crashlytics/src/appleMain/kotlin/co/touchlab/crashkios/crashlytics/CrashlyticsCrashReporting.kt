@file:OptIn(ExperimentalForeignApi::class, InternalCrashKiOSApi::class)

package co.touchlab.crashkios.crashlytics

import co.touchlab.crashkios.core.CrashReportingImplementation
import co.touchlab.crashkios.core.InternalCrashKiOSApi
import co.touchlab.crashkios.crashlytics.objc.CrashKiOSCrashlyticsSinkProtocol
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * The Crashlytics implementation for [co.touchlab.crashkios.core.CrashKiOS.configure]. It
 * wraps the Swift [sink] that forwards CrashKiOS events to Firebase Crashlytics.
 *
 * Configure CrashKiOS with this class once, from Swift, at startup. Do it after
 * `FirebaseApp.configure()` and before any Kotlin code runs:
 * ```swift
 * FirebaseApp.configure()
 * CrashKiOS.shared.configure(crashReporting: CrashlyticsCrashReporting(sink: CrashlyticsSink()))
 * ```
 * `CrashlyticsSink` ships in the `CrashKiOSCrashlytics` Swift package (this repo's
 * `Package.swift`). You can also copy it from the docs.
 *
 * Add `export("co.touchlab.crashkios:crashlytics")` AND `export("co.touchlab.crashkios:core")`
 * to your framework configuration for clean unmangled names.
 *
 * You can call `enableCrashlytics()` from shared startup code before or after this call.
 * CrashKiOS drops events that Kotlin sends before you configure a sink.
 */
public class CrashlyticsCrashReporting(private val sink: CrashKiOSCrashlyticsSinkProtocol) : CrashReportingImplementation {
    override fun install() {
        crashlyticsRegistry.register(sink) { throwable ->
            CrashlyticsKotlin.sendFatalException(throwable)
        }
        CrashlyticsKotlin.implementation = CrashlyticsCallsActual()
    }
}
