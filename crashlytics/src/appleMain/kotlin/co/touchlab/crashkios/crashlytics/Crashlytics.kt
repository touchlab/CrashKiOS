package co.touchlab.crashkios.crashlytics

import com.rickclephas.kmp.nsexceptionkt.core.wrapUnhandledExceptionHook

/**
 * Sets the unhandled exception hook such that all unhandled exceptions are logged to Crashlytics as fatal exceptions.
 * If an unhandled exception hook was already set, that hook will be invoked after the exception is logged.
 * Note: once the exception is logged the program will be terminated.
 * @see wrapUnhandledExceptionHook
 */
public fun setCrashlyticsUnhandledExceptionHook(): Unit = wrapUnhandledExceptionHook { throwable ->
    CrashlyticsKotlin.sendFatalException(throwable)
}
