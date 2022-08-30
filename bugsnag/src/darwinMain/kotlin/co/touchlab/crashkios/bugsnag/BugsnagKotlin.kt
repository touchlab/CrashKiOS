package co.touchlab.crashkios.bugsnag

import com.rickclephas.kmp.nsexceptionkt.core.asNSException
import com.rickclephas.kmp.nsexceptionkt.core.causes

actual object BugsnagKotlin {

    actual fun logMessage(message: String) {
        Bugsnag.leaveBreadcrumbWithMessage(message)
    }

    actual fun sendHandledException(throwable: Throwable) {
        sendException(throwable, true)
    }

    actual fun sendFatalException(throwable: Throwable) {
        sendException(throwable, false)
    }

    actual fun setCustomValue(key: String, value: Any, section: String) {
        Bugsnag.addMetadata(value, key, section)
    }

    private fun sendException(throwable: Throwable, handled: Boolean) {
        val exception = throwable.asNSException()
        val causes = throwable.causes.map { it.asNSException() }
        // Notify will persist unhandled events, so we can safely terminate afterwards.
        // https://github.com/bugsnag/bugsnag-cocoa/blob/6bcd46f5f8dc06ac26537875d501f02b27d219a9/Bugsnag/Client/BugsnagClient.m#L744
        Bugsnag.notify(exception) { event ->
            if (event == null) return@notify true

            if (handled) {
                event.severity = BSGSeverity.BSGSeverityWarning
            } else {
                event.unhandled = true
                event.severity = BSGSeverity.BSGSeverityError
            }

            if (causes.isNotEmpty()) {
                event.errors += causes.map { it.asBugsnagError() }
            }

            true
        }
    }

}