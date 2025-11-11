package co.touchlab.crashkios.bugsnag

import com.bugsnag.android.Bugsnag

actual class BugsnagCallsActual : BugsnagCalls {
    override fun logMessage(message: String) {
        Bugsnag.leaveBreadcrumb(message)
    }

    override fun sendHandledException(throwable: Throwable) {
        Bugsnag.notify(throwable)
    }

    override fun sendFatalException(throwable: Throwable) {
        Bugsnag.notify(throwable)
    }

    override fun setCustomValue(section: String, key: String, value: Any) {
        Bugsnag.addMetadata(section, key, value)
    }
}
