package co.touchlab.crashkios.bugsnag

expect object BugsnagKotlin {
    fun logMessage(message: String)
    fun sendHandledException(throwable: Throwable)
    fun sendFatalException(throwable: Throwable)
    fun setCustomValue(key: String, value: Any, section: String)
}