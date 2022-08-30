package co.touchlab.crashkios.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics

actual object CrashlyticsKotlin {
    actual fun logMessage(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    actual fun sendHandledException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    actual fun sendFatalException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    actual fun setCustomValue(key: String, value: Any) {
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