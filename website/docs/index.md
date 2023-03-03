---
sidebar_position: 1
title: Home 
---

# CrashKiOS - Crash reporting for Kotlin/iOS

Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are [Firebase Crashlytics](https://firebase.google.com/) and [Bugsnag](https://www.bugsnag.com/).

To use crash reporting with general logging support, check out [Kermit](https://github.com/touchlab/Kermit/).

If you're wondering *why* you need this library, please see [the problem](misc/THE_PROBLEM.md).

## NSExceptionKt

CrashKiOS and Kermit previously created 2 reports on a crash because none of the crash reporting clients had an obvious way to do one. [Rick Clephas](https://github.com/rickclephas) has done some excellent work figuring that out with [NSExceptionKt](https://github.com/rickclephas/NSExceptionKt). CrashKiOS now uses part of that library as a base and we've merged the cinterop from Kermit and NSExeptionKt to handle crashes as well as breadcrumb values and log statements.

## Getting Help

CrashKiOS support can be found in the Kotlin Community Slack, [request access here](http://slack.kotlinlang.org/). Post in the "[#touchlab-tools](https://kotlinlang.slack.com/archives/CTJB58X7X)" channel.

For direct assistance, please [contact Touchlab](https://go.touchlab.co/contactus-gh) to discuss support options.

