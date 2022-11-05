package co.touchlab.crashkios.crashlytics

import com.rickclephas.kmp.nsexceptionkt.core.asNSException
import com.rickclephas.kmp.nsexceptionkt.core.causes
import com.rickclephas.kmp.nsexceptionkt.core.wrapUnhandledExceptionHook
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSException
import platform.Foundation.NSNumber

/**
 * Sets the unhandled exception hook such that all unhandled exceptions are logged to Crashlytics as fatal exceptions.
 * If an unhandled exception hook was already set, that hook will be invoked after the exception is logged.
 * Note: once the exception is logged the program will be terminated.
 * @see wrapUnhandledExceptionHook
 */
public fun setCrashlyticsUnhandledExceptionHook(): Unit = wrapUnhandledExceptionHook { throwable ->
    CrashlyticsKotlin.sendFatalException(throwable)
}
