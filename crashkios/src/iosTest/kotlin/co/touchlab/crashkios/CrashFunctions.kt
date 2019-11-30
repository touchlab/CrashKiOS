package co.touchlab.crashkios

import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CrashFunctions {

    @Test
    fun setupCrashHandlerTest(){
        val m = MockCrashHandler()
        setupCrashHandler(m)
        assertFails {
            catchAndReport {
                throw IllegalStateException("Heyo")
            }
        }

        assertNotNull(m.throwable)
    }

    @Test
    fun providedHandler(){
        val m = MockCrashHandler()
        val providedHandler = MockCrashHandler()
        setupCrashHandler(m)
        assertFails {
            catchAndReport(providedHandler) {
                throw IllegalStateException("Heyo")
            }
        }

        assertNotNull(providedHandler.throwable)
        assertNull(m.throwable)
    }

    class MockCrashHandler:CrashHandler(){
        private val handled = AtomicReference<Throwable?>(null)
        override fun crash(t: Throwable) {
            handled.value = t.freeze()
        }

        val throwable:Throwable?
        get() = handled.value
    }
}