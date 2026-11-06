package co.touchlab.crashkios.crashlytics

public interface CrashlyticsCalls {
    public fun logMessage(message: String)
    public fun sendHandledException(throwable: Throwable)
    public fun sendFatalException(throwable: Throwable)
    public fun setCustomValue(key: String, value: Any)
    public fun setUserId(identifier: String)
    public fun setCollectionEnabled(enabled: Boolean)
}

public expect class CrashlyticsCallsActual() : CrashlyticsCalls {
    override fun logMessage(message: String)
    override fun sendHandledException(throwable: Throwable)
    override fun sendFatalException(throwable: Throwable)
    override fun setCustomValue(key: String, value: Any)
    override fun setUserId(identifier: String)
    override fun setCollectionEnabled(enabled: Boolean)
}
