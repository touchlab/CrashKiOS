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

import co.touchlab.crashkios.internal.maybeFreeze
import kotlin.native.concurrent.AtomicReference

open class CrashHandler {
    open fun crash(t: Throwable) {
        transformException(t){name, message, addresses ->
            crashParts(
                addresses,
                name,
                message
            )
        }
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
        handler.value = h.maybeFreeze()
    }

    fun myHandler(): CrashHandler = handler.value
}