package co.touchlab.crashkios.crashlytics

interface CrashlyticsCalls {
    fun logMessage(message: String)
    fun sendHandledException(throwable: Throwable)
    fun sendFatalException(throwable: Throwable)
    fun setCustomValue(key: String, value: Any)

    fun setUserId(identifier: String)
}

expect class CrashlyticsCallsActual() : CrashlyticsCalls