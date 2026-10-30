package co.touchlab.crashkios.bugsnag

public interface BugsnagCalls {
    public fun logMessage(message: String)

    public fun sendHandledException(throwable: Throwable)

    public fun sendFatalException(throwable: Throwable)

    public fun setCustomValue(section: String, key: String, value: Any)
}

public expect class BugsnagCallsActual() : BugsnagCalls {
    override fun logMessage(message: String)
    override fun sendHandledException(throwable: Throwable)
    override fun sendFatalException(throwable: Throwable)
    override fun setCustomValue(section: String, key: String, value: Any)
}
