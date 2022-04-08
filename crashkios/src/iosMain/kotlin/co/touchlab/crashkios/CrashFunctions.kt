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

fun setupCrashHandler(handler: CrashHandler){
    DefaultCrashHandler.crashHandler(handler)
    setupDefaultUnhandledExceptionHook()
}

fun setupDefaultUnhandledExceptionHook(){
    val unhandMe: ReportUnhandledExceptionHook = { t ->
        DefaultCrashHandler.handler.value.crash(t)
    }

    setUnhandledExceptionHook(unhandMe.maybeFreeze())
}

fun setupUnhandledExceptionHook(handler: CrashHandler){
    val unhandMe: ReportUnhandledExceptionHook = { t ->
        handler.crash(t)
    }

    setUnhandledExceptionHook(unhandMe.maybeFreeze())
}

fun <T> catchAndReport(handler: CrashHandler? = null, block:()->T):T{
    try {
        return block()
    } catch (t: Throwable) {
        (handler ?: DefaultCrashHandler.handler.value).crash(t)
        throw t
    }
}