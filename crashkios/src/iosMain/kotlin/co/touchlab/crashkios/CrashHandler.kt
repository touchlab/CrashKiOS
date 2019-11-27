/*
 * Copyright (C) 2019 Touchlab, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.touchlab.crashkios

import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.native.getStackTraceAddresses

open class CrashHandler {
    open fun crash(t: Throwable) {

        val addresses: List<Long> = t.getStackTraceAddresses()

        var index = 0

        val stackTrace = t.getStackTrace()
        for (i in 0 until stackTrace.size) {
            val frameString = stackTrace[i]
            if (
                !frameString.contains("kotlin.Exception")
                &&
                        !frameString.contains("Exception.<init>")
            ) {
                break
            }

            index++
        }

        val trimmedAddresses = addresses.subList(index, addresses.size)

        crashParts(
            trimmedAddresses,
            t::class.simpleName ?: "(Unknown Type)",
            t.message ?: ""
        )
    }

    open fun crashParts(addresses: List<Long>, exceptionType: String, message: String) {

    }
}

object LogCrashHandler : CrashHandler() {
    override fun crash(t: Throwable) {
        t.printStackTrace()
    }
}

object DefaultCrashHandler {
    internal val handler = AtomicReference<CrashHandler>(LogCrashHandler)
    fun crashHandler(h: CrashHandler) {
        handler.value = h.freeze()
    }

    fun myHandler(): CrashHandler = handler.value
}