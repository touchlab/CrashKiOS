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
 * Call this function in startup code on Android. Tests should generally skip it.
 *
 * On Apple targets this call alone is NOT enough. Configure CrashKiOS from Swift with
 * `CrashKiOS.shared.configure(crashReporting: CrashlyticsCrashReporting(sink: ...))`.
 * That call also sets the implementation, so this function is redundant there.
 *
 * You can still call this function in any order, because CrashKiOS looks up the sink on
 * every event. CrashKiOS drops events that Kotlin sends before `configure`.
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
