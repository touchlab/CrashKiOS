package co.touchlab.crashkios.bugsnag

interface BugsnagCalls {
    fun logMessage(message: String)

    fun sendHandledException(throwable: Throwable)

    fun sendFatalException(throwable: Throwable)

    fun setCustomValue(section: String, key: String, value: Any)
}

expect class BugsnagCallsActual() : BugsnagCalls
