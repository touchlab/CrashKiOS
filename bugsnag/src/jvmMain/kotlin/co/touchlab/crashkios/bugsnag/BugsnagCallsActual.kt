package co.touchlab.crashkios.bugsnag

/**
 * No-op implementation for targets without a Bugsnag SDK. Lets CrashKiOS be declared once in
 * `commonMain` in projects that also target JVM.
 */
actual class BugsnagCallsActual : BugsnagCalls {
    actual override fun logMessage(message: String) {
    }

    actual override fun sendHandledException(throwable: Throwable) {
    }

    actual override fun sendFatalException(throwable: Throwable) {
    }

    actual override fun setCustomValue(section: String, key: String, value: Any) {
    }
}
