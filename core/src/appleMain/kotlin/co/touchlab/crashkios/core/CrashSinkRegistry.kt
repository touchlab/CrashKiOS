package co.touchlab.crashkios.core

import kotlin.concurrent.AtomicInt
import kotlin.concurrent.AtomicReference

/**
 * Registration point shared by the CrashKiOS SDK modules: holds the Swift-implemented
 * sink and installs the unhandled-exception hook exactly once.
 *
 * Internal machinery for the crashlytics/bugsnag modules.
 */
@InternalCrashKiOSApi
public class CrashSinkRegistry<T : Any>(private val name: String) {
    private val sinkRef = AtomicReference<T?>(null)
    private val hookInstalled = AtomicInt(0)

    /** The registered sink, or null until `CrashKiOS.configure(...)` runs from Swift. */
    public val sink: T? get() = sinkRef.value

    /**
     * Returns the registered sink, or fails loud. For paths that only run after
     * [register] (such as the fatal hook), where a missing sink is a bug.
     */
    public fun requireSink(): T = sink ?: error(
        "CrashKiOS: no $name sink registered. Call CrashKiOS.configure(...) with a $name " +
            "implementation from Swift at startup (after the $name SDK is initialized).",
    )

    public fun register(sink: T, fatalHook: (Throwable) -> Unit) {
        sinkRef.value = sink
        // Guard: wrapping twice would chain the hook onto itself and double-report fatals.
        if (hookInstalled.compareAndSet(0, 1)) {
            wrapUnhandledExceptionHook(fatalHook)
        }
    }
}
