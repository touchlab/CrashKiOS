package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.core.InternalCrashKiOSApi
import co.touchlab.crashkios.core.ThreadSafeVar

public object BugsnagKotlin {
    @OptIn(InternalCrashKiOSApi::class)
    public var implementation: BugsnagCalls by ThreadSafeVar(EmptyCalls)

    public fun logMessage(message: String) {
        implementation.logMessage(message)
    }

    public fun sendHandledException(throwable: Throwable) {
        implementation.sendHandledException(throwable)
    }

    public fun sendFatalException(throwable: Throwable) {
        implementation.sendFatalException(throwable)
    }

    public fun setCustomValue(section: String, key: String, value: Any) {
        implementation.setCustomValue(section, key, value)
    }
}

/**
 * Call this function in startup code on Android. Tests should generally skip it.
 *
 * On Apple targets this call alone is NOT enough. Configure CrashKiOS from Swift with
 * `CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink: ...))`.
 * That call also sets the implementation, so this function is redundant there.
 *
 * You can still call this function in any order, because CrashKiOS looks up the sink on
 * every event. CrashKiOS drops events that Kotlin sends before `configure`.
 */
public fun enableBugsnag() {
    BugsnagKotlin.implementation = BugsnagCallsActual()
}

internal object EmptyCalls : BugsnagCalls {
    override fun logMessage(message: String) {
    }

    override fun sendHandledException(throwable: Throwable) {
    }

    override fun sendFatalException(throwable: Throwable) {
    }

    override fun setCustomValue(section: String, key: String, value: Any) {
    }
}
