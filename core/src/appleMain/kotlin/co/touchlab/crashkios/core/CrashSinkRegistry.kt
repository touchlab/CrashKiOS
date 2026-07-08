package co.touchlab.crashkios.core

import kotlin.concurrent.AtomicInt
import kotlin.concurrent.AtomicReference

/**
 * Registration point shared by the CrashKiOS SDK modules: holds the Swift-implemented
 * sink and installs the unhandled-exception hook exactly once.
 *
 * Internal machinery for the crashlytics/bugsnag modules — not API for apps.
 */
public class CrashSinkRegistry<T : Any>(private val name: String) {
    private val sinkRef = AtomicReference<T?>(null)
    private val hookInstalled = AtomicInt(0)

    /**
     * Returns the registered sink, or fails loud: a crash-reporting implementation
     * constructed without a sink would silently lose every event, which is worse
     * than crashing at startup with a clear message.
     */
    public fun requireSink(): T = sinkRef.value ?: error(
        "CrashKiOS: no $name sink registered. Call register${name}Sink() from Swift at " +
            "startup (after the $name SDK is initialized) before enabling CrashKiOS from Kotlin.",
    )

    public fun register(sink: T, fatalHook: (Throwable) -> Unit) {
        sinkRef.value = sink
        // Guard: wrapping twice would chain the hook onto itself and double-report fatals.
        if (hookInstalled.compareAndSet(0, 1)) {
            wrapUnhandledExceptionHook(fatalHook)
        }
    }
}
