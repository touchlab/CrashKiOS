package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.core.ThreadSafeVar

object BugsnagKotlin {
    var implementation: BugsnagCalls by ThreadSafeVar(EmptyCalls)

    fun logMessage(message: String) {
        implementation.logMessage(message)
    }

    fun sendHandledException(throwable: Throwable) {
        implementation.sendHandledException(throwable)
    }

    fun sendFatalException(throwable: Throwable) {
        implementation.sendFatalException(throwable)
    }

    fun setCustomValue(section: String, key: String, value: Any) {
        implementation.setCustomValue(section, key, value)
    }
}

/**
 * Call in startup code on Android. Tests should generally skip this.
 *
 * On Apple targets this alone is NOT enough: a Swift sink must be configured via
 * `CrashKiOS.configure(BugsnagCrashReporting(sink))` (which also sets the
 * implementation, making this call redundant there) — otherwise events are dropped
 * with an NSLog warning.
 */
fun enableBugsnag() {
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
