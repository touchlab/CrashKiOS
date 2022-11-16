---
sidebar_position: 1
title: CrashKiOS Home 
---

# CrashKiOS - Crash reporting for Kotlin/iOS

Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are [Firebase Crashlytics](https://firebase.google.com/) and [Bugsnag](https://www.bugsnag.com/).

To use crash reporting with general logging support, check out [Kermit](https://github.com/touchlab/Kermit/).

If you're wondering *why* you need this library, please see [the problem](THE_PROBLEM.md).

> ## Subscribe!
>
> We build solutions that get teams started smoothly with Kotlin Multiplatform Mobile and ensure their success in production. Join our community to learn how your peers are adopting KMM.
> [Sign up here](https://go.touchlab.co/newsletter-gh)!

## Crashlytics Usage

Add the dependency.

```kotlin
val commonMain by sourceSets.getting {
    dependencies {
        implementation("co.touchlab.crashkios:crashlytics:{{LATEST_GITHUB_VERSION}}")
    }
}
```

The library by default has noop implementations of the crash logging calls. This is because in test situations you generally don't want to interact with the crash logging. On iOS specifically, this will allow you to run tests without needing to link against the Crashlytics runtime library.

As a result, in the live app you need to initialize CrashKiOS. For both Android and iOS, you must call the following:

```kotlin
enableCrashlytics()
```

You sould generally do this as part of app initialization, after you make the calls to start Crashlytics itself.

On iOS, you should also set the unhandled exception hook:

```kotlin
setCrashlyticsUnhandledExceptionHook()
```

Once initialized, you call methods on `CrashlyticsKotlin`, from common code or platform-specific code.

```kotlin
CrashlyticsKotlin.logMessage("Some message")
CrashlyticsKotlin.sendHandledException(Exception("Some exception"))
CrashlyticsKotlin.sendFatalException(Exception("Some exception"))
CrashlyticsKotlin.setCustomValue("someKey", "someValue")
```

### Testing

Your test code should not call `enableCrashlytics()`. Before calling `enableCrashlytics()`, calls to `CrashlyticsKotlin` are all no-ops. Also, on iOS, avoiding `enableCrashlytics()` means you don't need to worry about Crashlytics linker issues.

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

To resolve this, you should tell the linker that Crashlytics will be added later. You can do that directly, or you can use our Gradle plugin. It will find all Xcode Frameworks being built by Kotlin and add the necessary linker arguments.

```kotlin
plugins {
  id("co.touchlab.crashkios.crashlyticslink") version "{{LATEST_GITHUB_VERSION}}"
}
```

### Crashlytics Sample

See [samples/sample-crashlytics](samples/sample-crashlytics).

## Bugsnag Usage

Add the dependency.

```kotlin
val commonMain by sourceSets.getting {
    dependencies {
        implementation("co.touchlab.crashkios:bugsnag:{{LATEST_GITHUB_VERSION}}")
    }
}
```

The library by default has noop implementations of the crash logging calls. This is because in test situations you generally don't want to interact with the crash logging. On iOS specifically, this will allow you to run tests without needing to link against the Bugsnag runtime library.

As a result, in the live app you need to initialize CrashKiOS. For both Android and iOS, you must call the following:

```kotlin
enableBugsnag()
```

You sould generally do this as part of app initialization, after you make the calls to start Bugsnag itself.

### iOS Only

Bugsnag is somewhat more complex than Crashlytics on iOS. On startup, the library needs to suppress an extra error report from being sent. That requires some extra calls, or you can use a helper function that will handle everything.

The detailed calls you need to make are the following:

In the iOS init, before starting Bugsnag, you need to call `configureBugsnag` with an instance of `BugsnagConfiguration`. The simplest way to get `BugsnagConfiguration` from Swift is by calling:

```swift
let config = BugsnagConfiguration.loadConfig()
```

#### Option 1: Manual Calls

Call `configureBugsnag` with that config. This *must* be called before starting Bugsnag.

```swift
BugsnagConfigKt.configureBugsnag(config: config)
```

Start Bugsnag

```swift
Bugsnag.start(with: config)
```

Then set the default exception handler hook

```swift
BugsnagConfigKt.setBugsnagUnhandledExceptionHook()
```

If you haven't done so, call:

```swift
BugsnagConfigKt.enableBugsnag()
```



#### Option 2: Helper Calls

You can call a single function that performs the 4 steps above.

```swift
BugsnagConfigKt.startBugsnag(config: config)
```

That function calls `configureBugsnag`, `Bugsnag.start`, `setBugsnagUnhandledExceptionHook`, and `enableBugsnag()`.

### Using the Library

Once initialized, you call methods on `BugsnagKotlin`

```kotlin
BugsnagKotlin.logMessage("Some message")
BugsnagKotlin.sendHandledException(Exception("Some exception"))
BugsnagKotlin.sendFatalException(Exception("Some exception"))
BugsnagKotlin.setCustomValue("someKey", "someValue")
```

### Testing

Your test code should not call `enableBugsnag()`. Before calling `enableBugsnag()`, calls to `BugsnagKotlin` are all no-ops. Also, on iOS, avoiding `enableBugsnag()` means you don't need to worry about Bugsnag linker issues.

### Linking

If you are using dynamic frameworks, you'll see a linker error when building your framework.

```
Undefined symbols for architecture x86_64:
  "_OBJC_CLASS_$_BugsnagFeatureFlag", referenced from:
      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)
  "_OBJC_CLASS_$_BugsnagStackframe", referenced from:
      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)
  "_OBJC_CLASS_$_BugsnagError", referenced from:
      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)
  "_OBJC_CLASS_$_Bugsnag", referenced from:
      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)
      objc-class-ref in libco.touchlab:kermit-bugsnag-cache.a(result.o)
ld: symbol(s) not found for architecture x86_64
```

To resolve this, you should tell the linker that Bugsnag will be added later. You can do that directly, or you can use our Gradle plugin. It will find all Xcode Frameworks being built by Kotlin and add the necessary linker arguments.

```kotlin
plugins {
  id("co.touchlab.crashkios.bugsnaglink") version "{{LATEST_GITHUB_VERSION}}"
}
```

### Bugsnag Sample

See [samples/sample-bugsnag](samples/sample-bugsnag).

## NSExceptionKt

CrashKiOS and Kermit previously created 2 reports on a crash because none of the crash reporting clients had an obvious way to do one. [Rick Clephas](https://github.com/rickclephas) has done some excellent work figuring that out with [NSExceptionKt](https://github.com/rickclephas/NSExceptionKt). CrashKiOS now uses part of that library as a base and we've merged the cinterop from Kermit and NSExeptionKt to handle crashes as well as breadcrumb values and log statements.

## Getting Help

CrashKiOS support can be found in the Kotlin Community Slack, [request access here](http://slack.kotlinlang.org/). Post in the "[#touchlab-tools](https://kotlinlang.slack.com/archives/CTJB58X7X)" channel.

For direct assistance, please [contact Touchlab](https://go.touchlab.co/contactus-gh) to discuss support options.
