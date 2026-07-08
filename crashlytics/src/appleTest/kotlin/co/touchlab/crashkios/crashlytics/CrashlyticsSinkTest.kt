@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.crashlytics

import co.touchlab.crashkios.crashlytics.objc.CrashKiOSCrashlyticsSinkProtocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSException
import platform.darwin.NSObject

// Kotlin stand-in for the Swift sink: exercises the same ObjC-protocol boundary and
// proves the module links with zero linker flags (the old -U / RND-91 failure mode).
private class FakeCrashlyticsSink :
    NSObject(),
    CrashKiOSCrashlyticsSinkProtocol {
    val logs = mutableListOf<String>()
    val handled = mutableListOf<Triple<String, String, List<*>>>()
    val fatals = mutableListOf<NSException>()
    val custom = mutableMapOf<String, Any?>()
    var userId: String? = null
    var collectionEnabled: Boolean? = null

    override fun logMessage(message: String) {
        logs += message
    }

    override fun recordHandledExceptionWithName(name: String, reason: String, stackAddresses: List<*>) {
        handled += Triple(name, reason, stackAddresses)
    }

    override fun recordFatalException(exception: NSException) {
        fatals += exception
    }

    override fun setCustomValue(value: Any?, forKey: String) {
        custom[forKey] = value
    }

    override fun setUserId(identifier: String) {
        userId = identifier
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        collectionEnabled = enabled
    }
}

class CrashlyticsSinkTest {
    @Test
    fun facadeForwardsToRegisteredSink() {
        val sink = FakeCrashlyticsSink()
        registerCrashlyticsSink(sink)

        CrashlyticsKotlin.logMessage("hello")
        CrashlyticsKotlin.setCustomValue("answer", "42")
        CrashlyticsKotlin.setUserId("user1")
        CrashlyticsKotlin.setCollectionEnabled(true)
        CrashlyticsKotlin.sendHandledException(RuntimeException("boom"))
        CrashlyticsKotlin.sendFatalException(RuntimeException("fatal", IllegalStateException("root cause")))

        assertEquals(listOf("hello"), sink.logs)
        assertEquals("42", sink.custom["answer"]?.toString())
        assertEquals("user1", sink.userId)
        assertEquals(true, sink.collectionEnabled)

        val (name, reason, addresses) = sink.handled.single()
        assertEquals("kotlin.RuntimeException", name)
        assertEquals("boom", reason)
        assertTrue(addresses.isNotEmpty(), "handled exception should carry stack addresses")

        val fatal = sink.fatals.single()
        assertEquals("kotlin.RuntimeException", fatal.name)
        assertTrue(fatal.reason!!.contains("fatal"))
        assertTrue(fatal.reason!!.contains("Caused by: kotlin.IllegalStateException: root cause"))
        assertTrue(fatal.callStackReturnAddresses.isNotEmpty(), "fatal exception should carry stack addresses")
    }
}
