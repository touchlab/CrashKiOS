package co.touchlab.crashkios.crashlytics

expect object CrashlyticsKotlin {
    fun logMessage(message: String)
    fun sendHandledException(throwable: Throwable)
    fun sendFatalException(throwable: Throwable)
    fun setCustomValue(key: String, value: Any)
}