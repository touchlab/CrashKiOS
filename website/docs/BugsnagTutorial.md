---
id: bugsnag
---

# Bugsnag

If you use Bugsnag and an uncaught exception gets thrown from shared Kotlin code running on iOS, the crash report generated 
won't be very helpful in determining the cause, see [The Problem](misc/THE_PROBLEM.md) for more details. CrashKiOS was made 
to remedy this issue and provide meaningful stack traces for Kotlin crashes. 

## Step 1 - Add Bugsnag to Your Apps
First you'll need to set up Bugsnag for your Android and iOS individually. Follow the steps in the [bugsnag docs](https://docs.bugsnag.com/platforms/)
to set up Bugsnag with your Android app the normal way then do the same for your iOS app. 

Make sure to force a crash on both apps from non-shared code and ensure the crash shows up on the Bugsnag Console. 
If you're testing a crash on an iOS simulator, you'll need to hit run to build your changes, then close and reopen the app. Otherwise,
Xcode will catch and swallow your crash and it won't get reported. You'll also need to reopen the app after crashing it because
crash reports are sent at app startup. You'll also need to make sure you aren't crashing the app at app start which can cause it to 
not get reported. Make sure the crash is triggered by some action like a button click. Also don't forget to set up automatic
dSYM uploading, so you can see proper stack traces for Swift code.

## Step 2 - Add CrashKiOS  
Once you've verified Bugsnag is working for both platforms, you can add the CrashKiOS dependency to `commonMain` in your
shared module. 
```kotlin
val commonMain by sourceSets.getting {
    dependencies {
        api("co.touchlab.crashkios:bugsnag:{{LATEST_GITHUB_VERSION}}") // More on why api later
    }
}
```
Then disable caching in your `gradle.properties` file. We're currently working to update things to avoid this, for now 
we need it to deal with iOS linking issues.
```
kotlin.native.cacheKind.iosX64=none
kotlin.native.cacheKind.iosSimulatorArm64=none
```

After a Gradle sync, make a call to `enableBugsnag()` somewhere in your startup code for each app. This switches from the
default no-op implementations to real implementations which avoid issues in testing. Depending on how the `cacheKind` issue 
above is fixed this may no longer be necessary. 

```kotlin
Bugsnag.sendHandledException(Exception("Some exception"))
```

On iOS, there's a few more things to do at startup for Bugsnag to work. These are all wrapped in a helper function `startBugsnag(config: BugsnagConfiguration)`.
Where `config` is a configuration created in Swift for your Bugsnag setup. If you don't need to do any other config for Bugsnag
you can create an empty config like this: 
```swift 
let config = BugsnagConfiguration.loadConfig()
```

Then, in your shared module's `build.gradle` you can expose the CrashKiOS api to Swift and call `startBugsnag()` directly. 
This is why we need to add the CrashKiOS dependency with `api()` rather than `implementation()`
```kotlin
cocoapods {
    framework {
        export("co.touchlab.crashkios:bugsnag:{{LATEST_GITHUB_VERSION}}")
    }
    ...
}
```

Now that it's exported, you should be able to call `startBugsnag()` from app startup in Swift: 
```swift
let config = BugsnagConfiguration.loadConfig()
BugsnagConfigKt.startBugsnag(config: config)
```

Once this is done, throw an uncaught exception from Kotlin code on iOS. Reminder this needes to happen through user action, 
not in startup code, and you need to relaunch the app after building and after crashing to send the report. The crash should show 
up on the dashboard and should now have Kotlin line numbers 

## Step 3 - Setup Dynamic Linking (Optional) 
If you're ok with using static frameworks for your shared code, you're done with setup. If you want to export a dynamic framework then you'll see an error like this: 
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

This is because on iOS only the definitions for Bugsnag are added when comiling the Kotlin code and building the framework. The binary (the actual Bugsnag library) isn't added until later when you build the iOS app. When building a dynamic framework, the Kotlin compile expects to be able to resolve everything, so you'll see the above error because Bugsnag isn't there yet.

To work around this, we need to tell the compiler that these symbols are find and will be there later. Doing it manually is a bit messy so you can just add our gradle plugin to handle it 
```kotlin
  id("co.touchlab.crashkios.bugsnaglink") version "{{LATEST_GITHUB_VERSION}}"
```

## Sending Extra Info to Bugsnag
CrashKiOS-bugsnag also provides shared code wrappers for manually sending info to Bugsnag. When there is a crash, 
these will show up in with the crash in the dashboard
```kotlin
BugsnagKotlin.logMessage("Some message")
BugsnagKotlin.sendHandledException(Exception("Some exception"))
BugsnagKotlin.sendFatalException(Exception("Some exception"))
BugsnagKotlin.setCustomValue("someKey", "someValue")
```