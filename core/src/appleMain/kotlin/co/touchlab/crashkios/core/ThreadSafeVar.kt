package co.touchlab.crashkios.core

import kotlin.concurrent.AtomicReference
import kotlin.reflect.KProperty

@InternalCrashKiOSApi
public actual class ThreadSafeVar<T> actual constructor(target: T) {
    private val atom = AtomicReference(target)

    public actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = atom.value

    public actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        atom.value = value
    }
}
