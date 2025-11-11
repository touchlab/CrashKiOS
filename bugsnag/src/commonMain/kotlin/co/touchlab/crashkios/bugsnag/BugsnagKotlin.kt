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
 * Call in startup code in an actual app. Tests should generally skip this. In Kotlin/Native, not calling this
 * for tests avoids linker issues.
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
