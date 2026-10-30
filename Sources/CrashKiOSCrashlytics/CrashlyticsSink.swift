import Foundation
import CrashKiOSCrashlyticsObjC
import FirebaseCrashlytics

/// Reference `CrashKiOSCrashlyticsSink` implementation.
///
/// Configure CrashKiOS in your `AppDelegate`, right after you configure Firebase and before
/// any Kotlin code runs:
/// ```swift
/// FirebaseApp.configure()
/// CrashKiOS.shared.configure(crashReporting: CrashlyticsCrashReporting(sink: CrashlyticsSink()))
/// ```
/// Add `export("co.touchlab.crashkios:crashlytics")` AND `export("co.touchlab.crashkios:core")`
/// to the framework for clean names.
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
        // This call persists synchronously. Crashlytics stores a single fatal per session,
        // so it does not report the termination abort that follows a second time.
        FIRCLSExceptionRecordNSException(exception)
    }

    public func setCustomValue(_ value: Any?, forKey key: String) {
        crashlytics.setCustomValue(value, forKey: key)
    }

    public func setUserId(_ identifier: String) {
        crashlytics.setUserID(identifier)
    }

    public func setCollectionEnabled(_ enabled: Bool) {
        crashlytics.setCrashlyticsCollectionEnabled(enabled)
    }
}
