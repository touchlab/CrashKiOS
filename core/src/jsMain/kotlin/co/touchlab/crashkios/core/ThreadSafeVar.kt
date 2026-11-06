package co.touchlab.crashkios.core

import kotlin.reflect.KProperty

@InternalCrashKiOSApi
public actual class ThreadSafeVar<T> actual constructor(private var target: T) {
    public actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = target

    public actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        target = value
    }
}
