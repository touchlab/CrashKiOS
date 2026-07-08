import Foundation
import CrashKiOSCrashlyticsObjC
import FirebaseCrashlytics

/// Reference `CrashKiOSCrashlyticsSink` implementation.
///
/// Register from your `AppDelegate`, right after Firebase is configured and before
/// any Kotlin code runs:
/// ```swift
/// FirebaseApp.configure()
/// CrashlyticsKt.registerCrashlyticsSink(sink: CrashlyticsSink())
/// ```
/// (The Kotlin function name depends on your framework's export configuration; add
/// `export("co.touchlab.crashkios:crashlytics")` to the framework for clean names.)
public final class CrashlyticsSink: NSObject, CrashKiOSCrashlyticsSink {

    private let crashlytics: Crashlytics

    public init(_ crashlytics: Crashlytics = Crashlytics.crashlytics()) {
        self.crashlytics = crashlytics
    }

    public func logMessage(_ message: String) {
        crashlytics.log(message)
    }

    public func recordHandledException(withName name: String, reason: String, stackAddresses: [NSNumber]) {
        let model = ExceptionModel(name: name, reason: reason)
        model.stackTrace = stackAddresses.map { StackFrame(address: $0.uintValue) }
        crashlytics.record(exceptionModel: model)
    }

    public func recordFatalException(_ exception: NSException) {
        // Persists synchronously; Crashlytics stores a single fatal per session, so the
        // termination abort that follows is not double-reported.
        FIRCLSExceptionRecordNSException(exception)
    }

    public func setCustomValue(_ value: Any?, forKey key: String) {
        crashlytics.setCustomValue(value as Any, forKey: key)
    }

    public func setUserId(_ identifier: String) {
        crashlytics.setUserID(identifier)
    }
}
