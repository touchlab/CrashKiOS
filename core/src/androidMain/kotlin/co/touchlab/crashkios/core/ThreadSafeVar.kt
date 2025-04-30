package co.touchlab.crashkios.core

import kotlin.reflect.KProperty

actual class ThreadSafeVar<T> actual constructor(@Volatile private var target: T) {
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = target

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        target = value
    }
}
