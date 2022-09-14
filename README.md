# CrashKiOS - Crash reporting for Kotlin/iOS

Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are [Firebase Crashlytics](https://firebase.google.com/) and [Bugsnag](https://www.bugsnag.com/).

To use crash reporting with general logging support, check out [Kermit](https://github.com/touchlab/Kermit/).

> ## **We're Hiring!**
>
> Touchlab is looking for a Mobile Developer, with Android/Kotlin experience, who is eager to dive into Kotlin Multiplatform Mobile (KMM) development. Come join the remote-first team putting KMM in production. [More info here](https://go.touchlab.co/careers-gh).

## The Problem

Crash reporter clients on iOS take the stack of active threads at the moment of crash. Kotlin on iOS, like Kotlin on the JVM, bubbles exceptions up until they are caught or reach the top of the call stack, at which point an unhandled exception hook is called. Because this differs from how iOS works, the crash report shows the point at which we call into Kotlin from Swift/Objc.

<img src="kotlinabort.png" alt="Abort report" style="zoom:50%;" />

We want to get the caught exception and record that instead:

<img src="kotlinlines.png" alt="Abort report" style="zoom: 67%;" />

That's what this library is for.

## Crashlytics Usage

Add the dependency.

```kotlin
val commonMain by sourceSets.getting {
    dependencies {
        implementation("co.touchlab.crashkios:crashlytics:x.y.z")
    }
}
```

For both Android and iOS, you must call the following:

```kotlin
enableCrashlytics()
```

On iOS, you should also set the unhandled exception hook:

```kotlin
setCrashlyticsUnhandledExceptionHook()
```

Once initialized, you call methods on `CrashlyticsKotlin`

```kotlin
CrashlyticsKotlin.logMessage("Some message")
CrashlyticsKotlin.sendHandledException(Exception("Some exception"))
CrashlyticsKotlin.sendFatalException(Exception("Some exception"))
CrashlyticsKotlin.setCustomValue("someKey", "someValue")
```

### Testing

You test code should not call `enableCrashlytics()`. Before calling `enableCrashlytics()`, calls to `CrashlyticsKotlin` are all no-ops. Also, on iOS, avoiding `enableCrashlytics()` means you don't need to worry about Crashlytics linker issues.

### Linking

If you are using dynamic frameworks, you'll see a linker error when building your framework.

```
Undefined symbols for architecture x86_64:
  "_OBJC_CLASS_$_FIRStackFrame", referenced from:
      objc-class-ref in result.o
  "_OBJC_CLASS_$_FIRExceptionModel", referenced from:
      objc-class-ref in result.o
  "_OBJC_CLASS_$_FIRCrashlytics", referenced from:
      objc-class-ref in result.o
  "_FIRCLSExceptionRecordNSException", referenced from:
      _co_touchlab_crashkios_crashlytics_FIRCLSExceptionRecordNSException_wrapper0 in result.o
ld: symbol(s) not found for architecture x86_64
```

To resolve this, you should tell the linker that Crashlytics will be added later. To do that, call `crashlyticsLinkerConfig()` in the `kotlin` section of your `build.gradle.kts`.
