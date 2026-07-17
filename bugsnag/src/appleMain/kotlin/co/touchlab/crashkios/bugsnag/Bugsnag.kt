@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.bugsnag.objc.CrashKiOSBugsnagSinkProtocol
import co.touchlab.crashkios.core.CrashReportingImplementation
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * The Bugsnag backend for [co.touchlab.crashkios.core.CrashKiOS.configure]. Wraps the
 * Swift-implemented [sink] that forwards CrashKiOS events to Bugsnag.
 *
 * Call once, from Swift, at startup. Start Bugsnag through the shipped sink so the
 * configuration suppresses the duplicate termination crash:
 * ```swift
 * let sink = BugsnagSink.start(BugsnagConfiguration.loadConfig())
 * CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink: sink))
 * ```
 * `BugsnagSink` ships in the `CrashKiOSBugsnag` Swift package (this repo's
 * `Package.swift`); custom sink implementations must call
 * `configureBugsnagForKotlin(config)` (from the same package) before `Bugsnag.start`
 * to keep the duplicate-crash suppression. Add `export("co.touchlab.crashkios:bugsnag")`
 * AND `export("co.touchlab.crashkios:core")` to your framework configuration for
 * clean unmangled names.
 *
 * Calling `enableBugsnag()` (or constructing `BugsnagCallsActual`) on an Apple target
 * WITHOUT having configured a sink fails fast with a descriptive error rather than
 * silently dropping crash reports.
 */
public class BugsnagCrashReporting(private val sink: CrashKiOSBugsnagSinkProtocol) : CrashReportingImplementation {
    override fun install() {
        bugsnagRegistry.register(sink, fatalHook(sink))
        BugsnagKotlin.implementation = BugsnagCallsActual()
    }
}

/**
 * The terminating-hook body: reports the exception, then marks the session so the
 * sink's OnSendError filter drops Bugsnag's own report of the termination abort that
 * follows. Only the terminating hook path marks: a direct sendFatalException() call
 * must NOT poison the session (the caller may keep running, and later genuine crashes
 * would be discarded).
 *
 * A plain function rather than an inline lambda so tests can exercise the
 * notify-then-mark sequencing directly, without going through the real
 * process-terminating unhandled-exception hook.
 */
internal fun fatalHook(sink: CrashKiOSBugsnagSinkProtocol): (Throwable) -> Unit = { throwable ->
    BugsnagKotlin.sendFatalException(throwable)
    sink.markFatalCrashRecorded()
}
