@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.bugsnag.objc.CrashKiOSBugsnagSinkProtocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSException
import platform.darwin.NSObject

// Kotlin stand-in for the Swift sink: exercises the same ObjC-protocol boundary and
// proves the module links with zero linker flags (the old -U / RND-91 failure mode).
private class FakeBugsnagSink :
    NSObject(),
    CrashKiOSBugsnagSinkProtocol {
    val breadcrumbs = mutableListOf<String>()
    val notifications = mutableListOf<Pair<List<*>, Boolean>>()
    val metadata = mutableListOf<Triple<Any?, String, String>>()
    var fatalMarks = 0

    // Call order across notify/mark — proves the hook sequences them correctly,
    // not just that both eventually happened.
    val events = mutableListOf<String>()

    override fun leaveBreadcrumb(message: String) {
        breadcrumbs += message
    }

    override fun markFatalCrashRecorded() {
        fatalMarks++
        events += "mark"
    }

    override fun notifyWithExceptions(exceptions: List<*>, handled: Boolean) {
        notifications += exceptions to handled
        events += "notify"
    }

    override fun addMetadata(value: Any, key: String, section: String) {
        metadata += Triple(value, key, section)
    }
}

class BugsnagSinkTest {
    // registerBugsnagSink() mutates the module-level `bugsnagRegistry` singleton. Safe
    // to call from every test that needs BugsnagKotlin's facade routed to its own sink
    // (sinkRef always overwrites) — NOT safe to rely on for the real unhandled-exception
    // hook actually firing with a given test's sink: hook-install is a once-ever guard,
    // permanently bound to whichever sink registered first. That's why fatalHookNotifiesThenMarksSession
    // below calls fatalHook() directly instead of triggering the real hook.
    @Test
    fun facadeForwardsToRegisteredSink() {
        val sink = FakeBugsnagSink()
        registerBugsnagSink(sink)

        BugsnagKotlin.logMessage("crumb")
        BugsnagKotlin.setCustomValue("section1", "key1", "value1")
        BugsnagKotlin.sendHandledException(RuntimeException("handled boom"))
        BugsnagKotlin.sendFatalException(RuntimeException("fatal boom", IllegalStateException("root cause")))

        assertEquals(listOf("crumb"), sink.breadcrumbs)

        val (value, key, section) = sink.metadata.single()
        assertEquals("value1", value?.toString())
        assertEquals("key1", key)
        assertEquals("section1", section)

        assertEquals(2, sink.notifications.size)
        val (handledExceptions, handledFlag) = sink.notifications[0]
        assertTrue(handledFlag)
        assertEquals(1, handledExceptions.size)
        assertEquals("kotlin.RuntimeException", (handledExceptions.single() as NSException).name)

        val (fatalExceptions, fatalFlag) = sink.notifications[1]
        assertEquals(false, fatalFlag)
        assertEquals(2, fatalExceptions.size, "fatal should carry the cause chain")
        assertEquals("kotlin.IllegalStateException", (fatalExceptions[1] as NSException).name)

        // Only the terminating unhandled-exception hook may mark the session —
        // a direct sendFatalException() must not (it would poison the OnSendError filter).
        assertEquals(0, sink.fatalMarks)
    }

    @Test
    fun fatalHookNotifiesThenMarksSession() {
        // fatalHook() is the terminating hook's actual body (see Bugsnag.kt), exercised
        // directly rather than through wrapUnhandledExceptionHook — invoking the real
        // hook would terminate the process.
        val sink = FakeBugsnagSink()
        // Rebinds BugsnagKotlin.implementation to this sink (register() always
        // overwrites sinkRef) so fatalHook's sendFatalException() call routes here
        // rather than to whatever sink an earlier test left registered. Does NOT wire
        // this sink into the real unhandled-exception hook — that binds once, to
        // whichever sink registers first — which is exactly why fatalHook is called
        // directly below instead of relying on the real hook firing.
        registerBugsnagSink(sink)

        fatalHook(sink)(RuntimeException("terminating boom"))

        assertEquals(1, sink.fatalMarks)
        assertEquals(1, sink.notifications.size)
        // Order matters: markFatalCrashRecorded() must run AFTER notify, never before —
        // marking first would let the OnSendError filter drop a report that was never sent.
        assertEquals(listOf("notify", "mark"), sink.events)
    }
}
