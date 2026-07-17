@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import co.touchlab.crashkios.bugsnag.objc.CrashKiOSBugsnagSinkProtocol
import co.touchlab.crashkios.core.CrashKiOS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSException
import platform.darwin.NSObject

class BugsnagSinkTest {
    @Test
    fun facadeForwardsToRegisteredSink() {
        val sink = FakeBugsnagSink()
        CrashKiOS.configure(BugsnagCrashReporting(sink))

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

        // Only the terminating unhandled-exception hook may mark the session,
        // but a direct sendFatalException() must not (it would poison the OnSendError filter).
        assertEquals(0, sink.fatalMarks)
    }

    @Test
    fun fatalHookNotifiesThenMarksSession() {
        val sink = FakeBugsnagSink()
        CrashKiOS.configure(BugsnagCrashReporting(sink))

        fatalHook(sink)(RuntimeException("terminating boom"))

        assertEquals(1, sink.fatalMarks)
        assertEquals(1, sink.notifications.size)
        // Order matters: markFatalCrashRecorded() must run AFTER notify, never before.
        // Marking first would let the OnSendError filter drop a report that was never sent.
        assertEquals(listOf("notify", "mark"), sink.events)
    }
}

private class FakeBugsnagSink :
    NSObject(),
    CrashKiOSBugsnagSinkProtocol {
    val breadcrumbs = mutableListOf<String>()
    val notifications = mutableListOf<Pair<List<*>, Boolean>>()
    val metadata = mutableListOf<Triple<Any?, String, String>>()
    var fatalMarks = 0

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
