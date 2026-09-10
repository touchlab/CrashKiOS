package co.touchlab.crashkios.crashlytics

/**
 * No-op implementation for targets without a Crashlytics SDK. Lets CrashKiOS be declared once in
 * `commonMain` in projects that also target JS.
 */
actual class CrashlyticsCallsActual : CrashlyticsCalls {
    actual override fun logMessage(message: String) {
    }

    actual override fun sendHandledException(throwable: Throwable) {
    }

    actual override fun sendFatalException(throwable: Throwable) {
    }

    actual override fun setCustomValue(key: String, value: Any) {
    }

    actual override fun setUserId(identifier: String) {
    }

    actual override fun setCollectionEnabled(enabled: Boolean) {
    }
}
