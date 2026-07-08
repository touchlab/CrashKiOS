//
//  AppDelegate.swift
//  CrashKiOSSampleIOS

// Copyright (c) 2021 Touchlab
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

import UIKit
import shared
import Bugsnag

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Configures Bugsnag for Kotlin crash handling, starts it, and registers the
        // sink with the Kotlin side (which installs the unhandled-exception hook).
        let sink = BugsnagSink.start(BugsnagConfiguration.loadConfig())
        BugsnagKt.registerBugsnagSink(sink: sink)

        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
}

// Copy of the reference sink from the CrashKiOS Swift package
// (Sources/CrashKiOSBugsnag/BugsnagSink.swift), inlined + the protocol declared in
// BridgingHeader.h so the sample doesn't need the package wired into the Xcode
// project. Prefer consuming the CrashKiOSBugsnag package product in a real app.
final class BugsnagSink: NSObject, CrashKiOSBugsnagSink {

    /// Configures `config` to suppress the duplicate termination crash that follows a
    /// recorded Kotlin fatal, starts Bugsnag, and returns the sink.
    static func start(_ config: BugsnagConfiguration) -> BugsnagSink {
        overrideOriginalUnhandledValue()
        config.addOnSendError { event in
            !event.unhandled || !event.featureFlags.contains { $0.name == kotlinCrashedFeatureFlag }
        }
        config.clearFeatureFlag(name: kotlinCrashedFeatureFlag)
        Bugsnag.start(with: config)
        return BugsnagSink()
    }

    func leaveBreadcrumb(_ message: String) {
        Bugsnag.leaveBreadcrumb(withMessage: message)
    }

    func notify(with exceptions: [NSException], handled: Bool) {
        guard let exception = exceptions.first else { return }
        Bugsnag.notify(exception) { event in
            if handled {
                event.severity = .warning
            } else {
                event.unhandled = true
                event.severity = .error
            }
            event.errors += exceptions.dropFirst().map(BugsnagError.init)
            return true
        }
    }

    func addMetadata(_ value: Any, key: String, section: String) {
        Bugsnag.addMetadata(value, key: key, section: section)
    }

    func markFatalCrashRecorded() {
        // Called only by the Kotlin unhandled-exception hook, right before termination —
        // never on direct sendFatalException() calls, which would poison the session and
        // make the OnSendError filter discard later genuine crashes.
        Bugsnag.addFeatureFlag(name: kotlinCrashedFeatureFlag)
    }
}

/// Feature flag used to mark the Kotlin termination crash.
private let kotlinCrashedFeatureFlag = "crashkios.kotlin_crashed"

/// In Bugsnag 6.26.2+ the `originalUnhandledValue` property prevents our synthetic
/// unhandled exceptions from being stored to disk; alias it to `unhandled`.
/// https://github.com/bugsnag/bugsnag-cocoa/pull/1549
private func overrideOriginalUnhandledValue() {
    guard let handledStateClass = NSClassFromString("BugsnagHandledState"),
          let originalMethod = class_getInstanceMethod(handledStateClass, NSSelectorFromString("originalUnhandledValue")),
          let method = class_getInstanceMethod(handledStateClass, NSSelectorFromString("unhandled"))
    else { return }
    method_setImplementation(originalMethod, method_getImplementation(method))
}

private extension BugsnagError {
    /// Creates a BugsnagError from a (Kotlin-synthesized) NSException.
    convenience init(_ exception: NSException) {
        self.init()
        errorClass = exception.name.rawValue
        errorMessage = exception.reason
        stacktrace = BugsnagStackframe.stackframes(withCallStackReturnAddresses: exception.callStackReturnAddresses)
        type = .cocoa
    }
}
