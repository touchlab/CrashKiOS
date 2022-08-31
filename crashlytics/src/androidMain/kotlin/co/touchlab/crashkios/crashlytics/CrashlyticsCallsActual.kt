package co.touchlab.crashkios.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics

actual class CrashlyticsCallsActual : CrashlyticsCalls {
    override fun logMessage(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun sendHandledException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun sendFatalException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun setCustomValue(key: String, value: Any) {
        when(value){
            is Boolean -> FirebaseCrashlytics.getInstance().setCustomKey(key, value)
            is Double -> FirebaseCrashlytics.getInstance().setCustomKey(key, value)
            is Float -> FirebaseCrashlytics.getInstance().setCustomKey(key, value)
            is Int -> FirebaseCrashlytics.getInstance().setCustomKey(key, value)
            is Long -> FirebaseCrashlytics.getInstance().setCustomKey(key, value)
            is String -> FirebaseCrashlytics.getInstance().setCustomKey(key, value)
            else -> {
                throw IllegalArgumentException("Custom value must be of type [Boolean, Double, Float, Int, Long, String]")
            }
        }
    }
}

internal actual fun setHook() {}