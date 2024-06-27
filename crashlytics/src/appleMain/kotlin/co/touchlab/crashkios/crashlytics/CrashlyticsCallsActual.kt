package co.touchlab.crashkios.crashlytics

import com.rickclephas.kmp.nsexceptionkt.core.asNSException
import com.rickclephas.kmp.nsexceptionkt.core.getFilteredStackTraceAddresses
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual class CrashlyticsCallsActual : CrashlyticsCalls {

    init {
        FIRCheckLinkDependencies()
    }

    override fun logMessage(message: String) {
        FIRCrashlyticsLog(message)
    }

    @OptIn(UnsafeNumber::class)
    override fun sendHandledException(throwable: Throwable) {
        val exceptionClassName = throwable::class.qualifiedName ?: throwable::class.simpleName ?: "kotlin.Throwable"
        FIRCrashlyticsRecordHandledException(exceptionClassName, throwable.message ?: "", throwable.getFilteredStackTraceAddresses().map {
            FIRStackFrameWithAddress(it.convert())
        })
    }

    override fun sendFatalException(throwable: Throwable) {
        val exception = throwable.asNSException(true)
        // The recorded exception is persisted, so we can safely terminate afterwards.
        // https://github.com/firebase/firebase-ios-sdk/blob/82f163bd86566f83c5d7572a1c2c0024a04eb4dc/Crashlytics/Crashlytics/Handlers/FIRCLSException.mm#L227
        tryFIRCLSExceptionRecordNSException(exception)
    }

    override fun setCustomValue(key: String, value: Any) {
        FIRCrashlyticsSetCustomValue(key, value)
    }

    override fun setUserId(identifier: String) {
        FIRCrashlyticsSetUserID(identifier)
    }
}
