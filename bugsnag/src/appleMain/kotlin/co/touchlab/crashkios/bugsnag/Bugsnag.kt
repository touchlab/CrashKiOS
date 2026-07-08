@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.bugsnag.objc.CrashKiOSBugsnagSinkProtocol
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * Registers the Swift-implemented [sink] that forwards CrashKiOS events to Bugsnag,
 * and installs the unhandled-exception hook so unhandled Kotlin exceptions are
 * reported as fatal crashes.
 *
 * Call once, from Swift, at startup. Start Bugsnag through the shipped sink so the
 * configuration suppresses the duplicate termination crash:
 * ```swift
 * let sink = BugsnagSink.start(BugsnagConfiguration.loadConfig())
 * BugsnagKt.registerBugsnagSink(sink: sink)
 * ```
 * `BugsnagSink` ships in the `CrashKiOSBugsnag` Swift package (this repo's
 * `Package.swift`); custom sink implementations must call
 * `configureBugsnagForKotlin(config)` (from the same package) before `Bugsnag.start`
 * to keep the duplicate-crash suppression. Add
 * `export("co.touchlab.crashkios:bugsnag")` to your framework configuration for
 * clean unmangled names.
 *
 * Replaces `startBugsnag()` / `configureBugsnag()` / `setBugsnagUnhandledExceptionHook()`,
 * which were removed along with the Bugsnag cinterop — Bugsnag start and configuration
 * now live in Swift.
 *
 * Calling `enableBugsnag()` (or constructing `BugsnagCallsActual`) on an Apple target
 * WITHOUT having registered a sink fails fast with a descriptive error rather than
 * silently dropping crash reports.
 */
public fun registerBugsnagSink(sink: CrashKiOSBugsnagSinkProtocol) {
    bugsnagRegistry.register(sink, fatalHook(sink))
    BugsnagKotlin.implementation = BugsnagCallsActual()
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
