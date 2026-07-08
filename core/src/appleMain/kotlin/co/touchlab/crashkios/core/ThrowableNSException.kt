/*
 * Vendored from NSExceptionKt v0.1.10 (https://github.com/rickclephas/NSExceptionKt),
 * adapted for the Kotlin 2.x memory model (freeze() removed) and the CrashKiOS package.
 *
 * MIT License
 * Copyright (c) 2022 Rick Clephas
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package co.touchlab.crashkios.core

import kotlin.concurrent.AtomicReference
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ReportUnhandledExceptionHook
import kotlin.native.setUnhandledExceptionHook
import kotlin.native.terminateWithUnhandledException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.convert
import platform.Foundation.NSException
import platform.Foundation.NSNumber
import platform.darwin.NSUInteger

/**
 * Returns a [NSException] representing `this` [Throwable].
 * If [appendCausedBy] is `true` then the name, message and stack trace
 * of the [causes][Throwable.cause] will be appended, else causes are ignored.
 */
@OptIn(UnsafeNumber::class, ExperimentalForeignApi::class)
public fun Throwable.asNSException(appendCausedBy: Boolean = false): NSException {
    val returnAddresses = getFilteredStackTraceAddresses().let { addresses ->
        if (!appendCausedBy) return@let addresses
        addresses.toMutableList().apply {
            for (cause in causes) {
                addAll(cause.getFilteredStackTraceAddresses(true, addresses))
            }
        }
    }.map {
        @Suppress("RemoveExplicitTypeArguments")
        NSNumber(unsignedInteger = it.convert<NSUInteger>())
    }
    return ThrowableNSException(throwableName, getReason(appendCausedBy), returnAddresses)
}

/**
 * Returns the qualifiedName or simpleName of `this` throwable's class,
 * or "Throwable" if both are `null`.
 *
 * Public so the SDK modules report the same name on the handled and fatal paths.
 */
public val Throwable.throwableName: String
    get() = this::class.qualifiedName ?: this::class.simpleName ?: "Throwable"

/**
 * Returns the [message][Throwable.message] of this throwable.
 * If [appendCausedBy] is `true` then caused by lines with the format
 * "Caused by: $name: $message" will be appended.
 */
internal fun Throwable.getReason(appendCausedBy: Boolean = false): String? {
    if (!appendCausedBy) return message
    return buildString {
        message?.let(::append)
        for (cause in causes) {
            if (isNotEmpty()) appendLine()
            append("Caused by: ")
            append(cause.throwableName)
            cause.message?.let { append(": $it") }
        }
    }.takeIf { it.isNotEmpty() }
}

internal class ThrowableNSException(name: String, reason: String?, private val returnAddresses: List<NSNumber>) :
    NSException(name, reason, null) {
    override fun callStackReturnAddresses(): List<NSNumber> = returnAddresses
}

/**
 * Returns a list with all the [causes][Throwable.cause].
 * The first element will be the cause, the second the cause of the cause, etc.
 * This function stops once a reference cycle is detected.
 */
public val Throwable.causes: List<Throwable> get() = buildList {
    val causes = mutableSetOf<Throwable>()
    var cause = cause
    while (cause != null && causes.add(cause)) {
        add(cause)
        cause = cause.cause
    }
}

/**
 * Returns a list of stack trace addresses representing
 * the stack trace of the constructor call to `this` [Throwable].
 * @param keepLastInit `true` to preserve the last constructor call, `false` to drop all constructor calls.
 * @param commonAddresses a list of addresses used to drop the last common addresses.
 * @see kotlin.getStackTraceAddresses
 */
@OptIn(ExperimentalNativeApi::class)
public fun Throwable.getFilteredStackTraceAddresses(keepLastInit: Boolean = false, commonAddresses: List<Long> = emptyList()): List<Long> =
    getStackTraceAddresses().dropInitAddresses(
        qualifiedClassName = this::class.qualifiedName ?: Throwable::class.qualifiedName!!,
        stackTrace = getStackTrace(),
        keepLast = keepLastInit,
    ).dropCommonAddresses(commonAddresses)

/**
 * Returns a list containing all addresses except for the first addresses
 * matching the constructor call of the [qualifiedClassName].
 * If [keepLast] is `true` the last constructor call won't be dropped.
 */
internal fun List<Long>.dropInitAddresses(qualifiedClassName: String, stackTrace: Array<String>, keepLast: Boolean = false): List<Long> {
    val exceptionInit = "kfun:$qualifiedClassName#<init>"
    var dropCount = 0
    var foundInit = false
    for (i in stackTrace.indices) {
        if (stackTrace[i].contains(exceptionInit)) {
            foundInit = true
        } else if (foundInit) {
            dropCount = i
            break
        }
    }
    if (keepLast) dropCount--
    return drop(kotlin.math.max(0, dropCount))
}

/**
 * Returns a list containing all addresses except for the last addresses that match with the [commonAddresses].
 */
internal fun List<Long>.dropCommonAddresses(commonAddresses: List<Long>): List<Long> {
    var i = commonAddresses.size
    if (i == 0) return this
    // `> 0`, not `>= 0`: at i == 0 the guard must stop the iteration instead of
    // decrementing past the start and reading commonAddresses[-1] (upstream fix).
    return dropLastWhile {
        i-- > 0 && commonAddresses[i] == it
    }
}

/**
 * Wraps the unhandled exception hook such that the provided [hook] is invoked
 * before the currently set unhandled exception hook is invoked.
 * Note: once the unhandled exception hook returns the program will be terminated.
 * @see setUnhandledExceptionHook
 * @see terminateWithUnhandledException
 */
@OptIn(ExperimentalNativeApi::class)
public fun wrapUnhandledExceptionHook(hook: (Throwable) -> Unit) {
    val prevHook = AtomicReference<ReportUnhandledExceptionHook?>(null)
    val wrappedHook: ReportUnhandledExceptionHook = {
        hook(it)
        prevHook.value?.invoke(it)
        terminateWithUnhandledException(it)
    }
    prevHook.value = setUnhandledExceptionHook(wrappedHook)
}
