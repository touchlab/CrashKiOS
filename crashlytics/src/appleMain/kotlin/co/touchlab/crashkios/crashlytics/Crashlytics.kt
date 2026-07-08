@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.crashlytics

import co.touchlab.crashkios.crashlytics.objc.CrashKiOSCrashlyticsSinkProtocol
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Registers the Swift-implemented [sink] that forwards CrashKiOS events to Firebase
 * Crashlytics, and installs the unhandled-exception hook so unhandled Kotlin
 * exceptions are logged as fatal crashes.
 *
 * Call once, from Swift, at startup — after `FirebaseApp.configure()` and before any
 * Kotlin code runs:
 * ```swift
 * FirebaseApp.configure()
 * CrashlyticsKt.registerCrashlyticsSink(sink: CrashlyticsSink())
 * ```
 * `CrashlyticsSink` ships in the `CrashKiOSCrashlytics` Swift package (this repo's
 * `Package.swift`), or copy it from the docs. Add
 * `export("co.touchlab.crashkios:crashlytics")` to your framework configuration for
 * clean unmangled names.
 *
 * Calling `enableCrashlytics()` (or constructing `CrashlyticsCallsActual`) on an Apple
 * target WITHOUT having registered a sink fails fast with a descriptive error rather
 * than silently dropping crash reports.
 */
public fun registerCrashlyticsSink(sink: CrashKiOSCrashlyticsSinkProtocol) {
    crashlyticsRegistry.register(sink) { throwable ->
        CrashlyticsKotlin.sendFatalException(throwable)
    }
    CrashlyticsKotlin.implementation = CrashlyticsCallsActual()
}

/**
 * The unhandled exception hook is now installed by [registerCrashlyticsSink]; this
 * function does nothing.
 */
@Deprecated(
    "registerCrashlyticsSink() installs the unhandled-exception hook; remove this call.",
)
public fun setCrashlyticsUnhandledExceptionHook() {
}
