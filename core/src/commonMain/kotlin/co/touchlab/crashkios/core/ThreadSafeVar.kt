package co.touchlab.crashkios.core

import kotlin.reflect.KProperty

expect class ThreadSafeVar<T>(target:T) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}
