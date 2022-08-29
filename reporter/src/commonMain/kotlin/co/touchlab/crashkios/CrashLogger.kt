package co.touchlab.crashkios

interface CrashInfoWriter {
    fun logMessage(message: String)
    fun sendHandledException(throwable: Throwable)
    fun sendFatalException(throwable: Throwable)
    fun setCustomValue(key: String, value: Any, section: String? = null)
}