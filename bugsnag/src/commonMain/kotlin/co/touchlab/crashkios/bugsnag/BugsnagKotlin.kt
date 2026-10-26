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
 * On Apple targets this alone is NOT enough: configure CrashKiOS from Swift with
 * `CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink: ...))`, which also
 * sets the implementation and makes this call redundant there. Calling it is still safe in any
 * order, since the sink is looked up on every event; events sent before `configure` are dropped.
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
