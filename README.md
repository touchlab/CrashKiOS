# CrashKiOS - Crash reporting for Kotlin/iOS

Thin library that provides symbolicated crash reports for Kotlin code on 
iOS. If you would like to log exceptions you can use CrashKiOS directly, but
you'll probably want to check out [Kermit](https://github.com/touchlab/Kermit/). Kermit is a logging library that
also includes implementations for Crashlytics and Bugsnag.

> ## Subscribe!
>
> We build solutions that get teams started smoothly with Kotlin Multiplatform Mobile and ensure their success in production. Join our community to learn how your peers are adopting KMM.
 [Sign up here](https://go.touchlab.co/newsletter)!

## The Problem

Kotlin's design has obviously been influenced by Java. In Java, exceptions
are a normal thing, and further in Kotlin, checked exceptions aren't a thing.
Crash reporters (Crashlytics, Bugsnag, etc) can take the unhandled exceptions
and provide the full stack trace.

On iOS, exceptions exist, but they're very much a special case. When a "crash" happens, 
the app stops, and crash reporting tools take the state of the application threads.
When calling into Kotlin code, if a crash happens in the Kotlin code, the exception 
bubbles back up to the iOS/Kotlin interface, at which point, if not @Throws, the app
is forcibly crashed. You can see the crash info from the local device and from the app store, 
assuming the user reported it, but crash reporting services like Crashlytics and Bugsnag 
only get the stack trace from the iOS/Kotlin interface. Not where the crash actually happened.

TL;DR, you'll see this:

![Abort report](kotlinabort.png)

You *want* to see this:

![Abort report](kotlinlines.png)

That's what this library is for.

## The Solution

Some crash reporting libraries support a way to symbolicate custom stack traces. The solution is relatively simple.
We get the pointers to functions called on a Kotlin Throwable, and send those to the crash reporting tools. With 
DSYM data, the crash reporting tools can trace that back to source code file and line numbers.
 
## Usage

You can use CrashKiOS directly, but it would be easier to integrate [Kermit](https://github.com/touchlab/Kermit/) and
use those implementations for Crashlytices and Bugsnag.

