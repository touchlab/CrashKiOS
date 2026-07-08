// Copyright (c) 2021 Touchlab
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

import UIKit
import shared
import Firebase
import FirebaseCrashlytics

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()
        // Registers the Crashlytics sink and installs the Kotlin unhandled-exception hook.
        CrashlyticsKt.registerCrashlyticsSink(sink: CrashlyticsSink())

        return true
    }
}

// Copy of the reference sink from the CrashKiOS Swift package
// (Sources/CrashKiOSCrashlytics/CrashlyticsSink.swift), inlined + the protocol
// declared in BridgingHeader.h so the sample doesn't need the package wired into
// the Xcode project. Prefer consuming the package product in a real app (see the
// ios-spm sample).
final class CrashlyticsSink: NSObject, CrashKiOSCrashlyticsSink {

    func logMessage(_ message: String) {
        Crashlytics.crashlytics().log(message)
    }

    func recordHandledException(withName name: String, reason: String, stackAddresses: [NSNumber]) {
        let model = ExceptionModel(name: name, reason: reason)
        model.stackTrace = stackAddresses.map { StackFrame(address: $0.uintValue) }
        Crashlytics.crashlytics().record(exceptionModel: model)
    }

    func recordFatalException(_ exception: NSException) {
        FIRCLSExceptionRecordNSException(exception)
    }

    func setCustomValue(_ value: Any?, forKey key: String) {
        Crashlytics.crashlytics().setCustomValue(value as Any, forKey: key)
    }

    func setUserId(_ identifier: String) {
        Crashlytics.crashlytics().setUserID(identifier)
    }
}

// Private Crashlytics SPI that records a FATAL exception and persists it synchronously.
// Resolved hard at app link time against the FirebaseCrashlytics binary.
@_silgen_name("FIRCLSExceptionRecordNSException")
private func FIRCLSExceptionRecordNSException(_ exception: NSException)
