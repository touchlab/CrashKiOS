package co.touchlab.crashkios.bugsnag

import com.bugsnag.android.Bugsnag

actual object BugsnagKotlin {

    actual fun logMessage(message: String) {
        Bugsnag.leaveBreadcrumb(message)
    }

    actual fun sendHandledException(throwable: Throwable) {
        Bugsnag.notify(throwable)
    }

    actual fun sendFatalException(throwable: Throwable) {
        Bugsnag.notify(throwable)
    }

    actual fun setCustomValue(key: String, value: Any, section: String) {
        Bugsnag.addMetadata(section, key, value)
    }

}