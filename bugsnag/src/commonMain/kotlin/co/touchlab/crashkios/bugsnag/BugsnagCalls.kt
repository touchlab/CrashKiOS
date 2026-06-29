package co.touchlab.crashkios.bugsnag

interface BugsnagCalls {
    fun logMessage(message: String)

    fun sendHandledException(throwable: Throwable)

    fun sendFatalException(throwable: Throwable)

    fun setCustomValue(section: String, key: String, value: Any)
}

expect class BugsnagCallsActual() : BugsnagCalls {
    override fun logMessage(message: String)
    override fun sendHandledException(throwable: Throwable)
    override fun sendFatalException(throwable: Throwable)
    override fun setCustomValue(section: String, key: String, value: Any)
}
