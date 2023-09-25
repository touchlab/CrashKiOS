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
}

/**
 * Call in startup code in an actual app. Tests should generally skip this. In Kotlin/Native, not calling this
 * for tests avoids linker issues.
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
}