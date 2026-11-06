package co.touchlab.crashkios.core

import kotlin.reflect.KProperty

@InternalCrashKiOSApi
public expect class ThreadSafeVar<T>(target: T) {
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): T

    public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}
