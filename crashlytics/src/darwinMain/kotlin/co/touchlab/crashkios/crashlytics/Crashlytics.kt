package co.touchlab.crashkios.crashlytics

import com.rickclephas.kmp.nsexceptionkt.core.asNSException
import com.rickclephas.kmp.nsexceptionkt.core.causes
import com.rickclephas.kmp.nsexceptionkt.core.wrapUnhandledExceptionHook
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSException
import platform.Foundation.NSNumber

/**
 * Defines strategies for logging [causes][Throwable.cause].
 */
public enum class CausedByStrategy {
    /**
     * Causes will be ignored,
     * only the main [Throwable] is logged as a fatal exception.
     */
    IGNORE,
    /**
     * Causes are appended to the main [Throwable]
     * and logged as a single fatal exception.
     */
    APPEND,
    /**
     * All causes are logged as non-fatal exceptions
     * before the main [Throwable] is logged as a fatal exception.
     */
    LOG_NON_FATAL
}

/**
 * Sets the unhandled exception hook such that all unhandled exceptions are logged to Crashlytics as fatal exceptions.
 * If an unhandled exception hook was already set, that hook will be invoked after the exception is logged.
 * Note: once the exception is logged the program will be terminated.
 * @param causedByStrategy the strategy used to log [causes][Throwable.cause].
 * @see wrapUnhandledExceptionHook
 */
public fun setCrashlyticsUnhandledExceptionHook(
    causedByStrategy: CausedByStrategy = CausedByStrategy.IGNORE
): Unit = wrapUnhandledExceptionHook { throwable ->
    val crashlytics = FIRCrashlytics.crashlytics()
    if (causedByStrategy == CausedByStrategy.LOG_NON_FATAL) {
        throwable.causes.asReversed().forEach { cause ->
            crashlytics.recordExceptionModel(cause.asNSException().asFIRExceptionModel())
        }
    }
    val exception = throwable.asNSException(causedByStrategy == CausedByStrategy.APPEND)
    // The recorded exception is persisted, so we can safely terminate afterwards.
    // https://github.com/firebase/firebase-ios-sdk/blob/82f163bd86566f83c5d7572a1c2c0024a04eb4dc/Crashlytics/Crashlytics/Handlers/FIRCLSException.mm#L227
    FIRCLSExceptionRecordNSException(exception)
}

/**
 * Converts `this` [NSException] to a [FIRExceptionModel].
 * An empty string is used as reason in case [reason][NSException.reason] is `null`.
 */
@OptIn(UnsafeNumber::class)
internal fun NSException.asFIRExceptionModel(): FIRExceptionModel = FIRExceptionModel(
    name ?: "Throwable", reason ?: ""
).apply {
    stackTrace = callStackReturnAddresses.map {
        FIRStackFrame.stackFrameWithAddress((it as NSNumber).unsignedIntegerValue)
    }
}
