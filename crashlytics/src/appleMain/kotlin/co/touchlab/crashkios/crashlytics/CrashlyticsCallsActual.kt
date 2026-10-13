@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.crashlytics

import co.touchlab.crashkios.core.CrashSinkRegistry
import co.touchlab.crashkios.core.asNSException
import co.touchlab.crashkios.core.getFilteredStackTraceAddresses
import co.touchlab.crashkios.core.throwableName
import co.touchlab.crashkios.crashlytics.objc.CrashKiOSCrashlyticsSinkProtocol
import kotlinx.cinterop.ExperimentalForeignApi

internal val crashlyticsRegistry = CrashSinkRegistry<CrashKiOSCrashlyticsSinkProtocol>("Crashlytics")

actual class CrashlyticsCallsActual : CrashlyticsCalls {
    // Fail-fast: an implementation constructed without a registered Swift sink would
    // silently lose every event. The pre-1.0 cinterop code failed just as loudly at
    // this point via FIRCheckLinkDependencies when Firebase wasn't linked.
    private val sink: CrashKiOSCrashlyticsSinkProtocol = crashlyticsRegistry.requireSink()

    actual override fun logMessage(message: String) {
        sink.logMessage(message)
    }

    actual override fun sendHandledException(throwable: Throwable) {
        sink.recordHandledExceptionWithName(
            name = throwable.throwableName,
            reason = throwable.message ?: "",
            stackAddresses = throwable.getFilteredStackTraceAddresses(),
        )
    }

    actual override fun sendFatalException(throwable: Throwable) {
        // The sink persists synchronously (FIRCLSExceptionRecordNSException),
        // so the caller can safely terminate afterwards.
        sink.recordFatalException(throwable.asNSException(true))
    }

    actual override fun setCustomValue(key: String, value: Any) {
        sink.setCustomValue(value, forKey = key)
    }

    actual override fun setUserId(identifier: String) {
        sink.setUserId(identifier)
    }

    actual override fun setCollectionEnabled(enabled: Boolean) {
        sink.setCollectionEnabled(enabled)
    }
}
