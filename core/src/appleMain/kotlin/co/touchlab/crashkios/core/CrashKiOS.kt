package co.touchlab.crashkios.core

/**
 * A crash-reporting backend that knows how to wire itself into CrashKiOS. Each SDK
 * module (bugsnag, crashlytics, ...) ships one implementation, constructed with its
 * Swift-side sink — see `BugsnagCrashReporting`/`CrashlyticsCrashReporting`.
 */
public fun interface CrashReportingImplementation {
    public fun install()
}

/**
 * Single configuration entry point for CrashKiOS. Which backend you get is a choice
 * of [CrashReportingImplementation] value passed in, not a choice of which function
 * you call — call this the same way regardless of backend. Kotlin `object`s bridge to
 * Swift as a `shared` singleton:
 * ```swift
 * CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink: sink))
 * ```
 * Requires `export("co.touchlab.crashkios:core")` (in addition to exporting the
 * backend module) in your framework configuration for clean, unmangled Swift names.
 */
public object CrashKiOS {
    public fun configure(crashReporting: CrashReportingImplementation) {
        crashReporting.install()
    }
}
