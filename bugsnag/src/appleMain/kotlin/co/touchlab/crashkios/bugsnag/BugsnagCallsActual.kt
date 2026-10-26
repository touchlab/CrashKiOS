@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.bugsnag.objc.CrashKiOSBugsnagSinkProtocol
import co.touchlab.crashkios.core.CrashSinkRegistry
import co.touchlab.crashkios.core.asNSException
import co.touchlab.crashkios.core.causes
import kotlinx.cinterop.ExperimentalForeignApi

internal val bugsnagRegistry = CrashSinkRegistry<CrashKiOSBugsnagSinkProtocol>("Bugsnag")

actual class BugsnagCallsActual : BugsnagCalls {
    private val sink get() = bugsnagRegistry.sink

    actual override fun logMessage(message: String) {
        sink?.leaveBreadcrumb(message)
    }

    actual override fun sendHandledException(throwable: Throwable) = sendException(throwable, true)

    actual override fun sendFatalException(throwable: Throwable) = sendException(throwable, false)

    actual override fun setCustomValue(section: String, key: String, value: Any) {
        sink?.addMetadata(value, key = key, section = section)
    }

    private fun sendException(throwable: Throwable, handled: Boolean) {
        val sink = sink ?: return
        val exceptions = listOf(throwable.asNSException()) + throwable.causes.map { it.asNSException() }
        // The sink persists unhandled events synchronously (Bugsnag.notify),
        // so the caller can safely terminate afterwards.
        sink.notifyWithExceptions(exceptions, handled = handled)
    }
}
