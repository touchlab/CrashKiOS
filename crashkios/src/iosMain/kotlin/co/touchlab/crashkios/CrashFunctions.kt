package co.touchlab.crashkios

import kotlin.native.concurrent.freeze

fun setupCrashHandler(handler: CrashHandler){
    DefaultCrashHandler.crashHandler(handler)
    setupDefaultUnhandledExceptionHook()
}

fun setupDefaultUnhandledExceptionHook(){
    val unhandMe: ReportUnhandledExceptionHook = { t ->
        DefaultCrashHandler.handler.value.crash(t)
    }

    setUnhandledExceptionHook(unhandMe.freeze())
}

fun setupUnhandledExceptionHook(handler: CrashHandler){
    val unhandMe: ReportUnhandledExceptionHook = { t ->
        handler.crash(t)
    }

    setUnhandledExceptionHook(unhandMe.freeze())
}

fun <T> catchAndReport(handler: CrashHandler? = null, block:()->T):T{
    println("cr1")
    try {
        return block()
    } catch (t: Throwable) {
        println("cr2")
        DefaultCrashHandler.handler.value.crash(t)
//        (handler ?: DefaultCrashHandler.handler.value).crash(t)
        throw t
    }
}