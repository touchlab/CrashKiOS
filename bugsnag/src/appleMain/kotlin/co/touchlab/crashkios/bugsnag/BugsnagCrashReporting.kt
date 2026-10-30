@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.bugsnag.objc.CrashKiOSBugsnagSinkProtocol
import co.touchlab.crashkios.core.CrashReportingImplementation
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * The Bugsnag implementation for [co.touchlab.crashkios.core.CrashKiOS.configure]. It wraps
 * the Swift [sink] that forwards CrashKiOS events to Bugsnag.
 *
 * Configure CrashKiOS with this class once, from Swift, at startup. Start Bugsnag through
 * the shipped sink. The sink prepares the configuration to suppress the duplicate
 * termination crash:
 * ```swift
 * let sink = BugsnagSink.start(BugsnagConfiguration.loadConfig())
 * CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink: sink))
 * ```
 * `BugsnagSink` ships in the `CrashKiOSBugsnag` Swift package (this repo's `Package.swift`).
 * A custom sink MUST call `configureBugsnagForKotlin(config)` from the same package
 * before `Bugsnag.start`. This keeps the duplicate-crash suppression.
 *
 * Add `export("co.touchlab.crashkios:bugsnag")` AND `export("co.touchlab.crashkios:core")`
 * to your framework configuration for clean unmangled names.
 *
 * You can call `enableBugsnag()` from shared startup code before or after this call.
 * CrashKiOS drops events that Kotlin sends before you configure a sink.
 */
public class BugsnagCrashReporting(private val sink: CrashKiOSBugsnagSinkProtocol) : CrashReportingImplementation {
    override fun install() {
        bugsnagRegistry.register(sink, fatalHook { bugsnagRegistry.requireSink() })
        BugsnagKotlin.implementation = BugsnagCallsActual()
    }
}

/**
 * Body of the terminating unhandled-exception hook. It reports the exception, then marks
 * the session. The mark makes the sink's OnSendError filter drop Bugsnag's own report of
 * the termination abort that follows.
 *
 * Only this hook marks the session. A direct `sendFatalException()` call must NOT mark it,
 * because the caller may keep running and the filter would discard later genuine crashes.
 *
 * The function takes the sink as a lambda, not as a value. CrashKiOS installs the hook
 * once, but `configure()` may run again with a different sink. Marking the session on a replaced
 * sink would silence the wrong Bugsnag session.
 *
 * This is a plain function, not an inline lambda. Tests can then check the notify-then-mark
 * sequence without the real process-terminating unhandled-exception hook.
 */
internal fun fatalHook(sink: () -> CrashKiOSBugsnagSinkProtocol): (Throwable) -> Unit = { throwable ->
    BugsnagKotlin.sendFatalException(throwable)
    sink().markFatalCrashRecorded()
}
