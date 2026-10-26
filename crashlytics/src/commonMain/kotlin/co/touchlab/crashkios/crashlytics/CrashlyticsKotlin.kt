package co.touchlab.crashkios.crashlytics

import co.touchlab.crashkios.core.ThreadSafeVar

object CrashlyticsKotlin {
    var implementation: CrashlyticsCalls by ThreadSafeVar(EmptyCalls)

    fun logMessage(message: String) {
        implementation.logMessage(message)
    }

    fun sendHandledException(throwable: Throwable) {
        implementation.sendHandledException(throwable)
    }

    fun sendFatalException(throwable: Throwable) {
        implementation.sendFatalException(throwable)
    }

    fun setCustomValue(key: String, value: Any) {
        implementation.setCustomValue(key, value)
    }

    fun setUserId(identifier: String) {
        implementation.setUserId(identifier)
    }

    fun setCollectionEnabled(enabled: Boolean) {
        implementation.setCollectionEnabled(enabled)
    }
}

/**
 * Call in startup code on Android. Tests should generally skip this.
 *
 * On Apple targets this alone is NOT enough: configure CrashKiOS from Swift with
 * `CrashKiOS.shared.configure(crashReporting: CrashlyticsCrashReporting(sink: ...))`, which also
 * sets the implementation and makes this call redundant there. Calling it is still safe in any
 * order, since the sink is looked up on every event; events sent before `configure` are dropped.
 */
fun enableCrashlytics() {
    CrashlyticsKotlin.implementation = CrashlyticsCallsActual()
}

internal object EmptyCalls : CrashlyticsCalls {
    override fun logMessage(message: String) {
    }

    override fun sendHandledException(throwable: Throwable) {
    }

    override fun sendFatalException(throwable: Throwable) {
    }

    override fun setCustomValue(key: String, value: Any) {
    }

    override fun setUserId(identifier: String) {
    }

    override fun setCollectionEnabled(enabled: Boolean) {
    }
}
