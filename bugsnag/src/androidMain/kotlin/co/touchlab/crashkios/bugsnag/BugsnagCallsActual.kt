package co.touchlab.crashkios.bugsnag

import com.bugsnag.android.Bugsnag

actual class BugsnagCallsActual : BugsnagCalls {
    actual override fun logMessage(message: String) {
        Bugsnag.leaveBreadcrumb(message)
    }

    actual override fun sendHandledException(throwable: Throwable) {
        Bugsnag.notify(throwable)
    }

    actual override fun sendFatalException(throwable: Throwable) {
        Bugsnag.notify(throwable)
    }

    actual override fun setCustomValue(section: String, key: String, value: Any) {
        Bugsnag.addMetadata(section, key, value)
    }
}
