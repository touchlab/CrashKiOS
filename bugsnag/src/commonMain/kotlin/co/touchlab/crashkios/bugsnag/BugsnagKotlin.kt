package co.touchlab.crashkios.bugsnag

expect object BugsnagKotlin {
    fun logMessage(message: String)
    fun sendHandledException(throwable: Throwable)
    fun sendFatalException(throwable: Throwable)
    fun setCustomValue(section: String, key: String, value: Any)
}