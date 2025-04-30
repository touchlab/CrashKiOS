package co.touchlab.crashkios.core

import kotlin.concurrent.AtomicReference
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.concurrent.freeze
import kotlin.reflect.KProperty

@OptIn(FreezingIsDeprecated::class, ExperimentalNativeApi::class)
actual class ThreadSafeVar<T> actual constructor(target: T) {
    private val atom = AtomicReference(target)

    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = atom.value

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (Platform.memoryModel == MemoryModel.STRICT) {
            value.freeze()
        }
        atom.value = value
    }
}
