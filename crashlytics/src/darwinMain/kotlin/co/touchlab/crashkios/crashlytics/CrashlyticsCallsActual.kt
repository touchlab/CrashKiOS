package co.touchlab.crashkios.crashlytics

import com.rickclephas.kmp.nsexceptionkt.core.asNSException

actual class CrashlyticsCallsActual : CrashlyticsCalls {
    override fun logMessage(message: String) {
        FIRCrashlytics.crashlytics().log(message)
    }

    override fun sendHandledException(throwable: Throwable) {
        val exception = throwable.asNSException(true)
        // The recorded exception is persisted, so we can safely terminate afterwards.
        // https://github.com/firebase/firebase-ios-sdk/blob/82f163bd86566f83c5d7572a1c2c0024a04eb4dc/Crashlytics/Crashlytics/Handlers/FIRCLSException.mm#L227
        FIRCLSExceptionRecordNSException(exception)
    }

    override fun sendFatalException(throwable: Throwable) {
        val exception = throwable.asNSException(true)
        // The recorded exception is persisted, so we can safely terminate afterwards.
        // https://github.com/firebase/firebase-ios-sdk/blob/82f163bd86566f83c5d7572a1c2c0024a04eb4dc/Crashlytics/Crashlytics/Handlers/FIRCLSException.mm#L227
        FIRCLSExceptionRecordNSException(exception)
    }

    override fun setCustomValue(key: String, value: Any) {
        FIRCrashlytics.crashlytics().setCustomValue(value, key)
    }
}

internal actual fun setHook() {
    setCrashlyticsUnhandledExceptionHook()
}