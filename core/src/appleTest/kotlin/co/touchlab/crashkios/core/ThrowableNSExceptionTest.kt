package co.touchlab.crashkios.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ThrowableNSExceptionTest {
    @Test
    fun dropCommonAddressesDropsMatchingTail() {
        assertEquals(listOf(9L), listOf(9L, 5L, 7L).dropCommonAddresses(listOf(5L, 7L)))
    }

    @Test
    fun dropCommonAddressesSurvivesReceiverLongerThanCommons() {
        // Regression: the pre-fix guard (i-- >= 0) read commonAddresses[-1] here.
        assertEquals(listOf(1L, 9L), listOf(1L, 9L, 5L, 7L).dropCommonAddresses(listOf(5L, 7L)))
    }

    @Test
    fun dropCommonAddressesFullMatch() {
        assertEquals(emptyList(), listOf(5L, 7L).dropCommonAddresses(listOf(5L, 7L)))
    }

    @Test
    fun dropCommonAddressesNoMatchAndEmptyCommons() {
        assertEquals(listOf(1L, 2L), listOf(1L, 2L).dropCommonAddresses(listOf(9L)))
        assertEquals(listOf(1L, 2L), listOf(1L, 2L).dropCommonAddresses(emptyList()))
    }

    @Test
    fun asNSExceptionWithConstructorBuiltCauseDoesNotThrow() {
        // End-to-end regression for the crash-in-the-crash-handler path.
        class WrappedError(msg: String) : Exception(msg, RuntimeException("io"))
        val exception = WrappedError("outer").asNSException(appendCausedBy = true)
        assertEquals("Caused by", exception.reason?.substringAfter("outer\n")?.substringBefore(":"))
    }

    @Test
    fun requireSinkFailsLoudWhenUnregistered() {
        val registry = CrashSinkRegistry<Any>("Crashlytics")
        val error = assertFailsWith<IllegalStateException> { registry.requireSink() }
        assertEquals(true, error.message?.contains("CrashKiOS.configure"))
    }
}
