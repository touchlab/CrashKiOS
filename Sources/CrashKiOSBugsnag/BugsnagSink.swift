import Bugsnag
import CrashKiOSBugsnagObjC
import Foundation
import ObjectiveC

/// Feature flag that marks the Kotlin termination crash.
/// This flag is public, so custom `CrashKiOSBugsnagSink` implementations can follow the same contract.
public let kotlinCrashedFeatureFlag = "crashkios.kotlin_crashed"

/// Prepares `config` for Kotlin crash handling.
///
/// This function does two things:
/// - It installs an OnSendError filter. The filter suppresses the duplicate termination crash that follows a recorded Kotlin fatal.
/// - It works around Bugsnag 6.26.2+, which refuses to persist synthetic unhandled events.
///
/// Custom sink implementations MUST call this function before `Bugsnag.start(with: config)`.
/// If they do not, Bugsnag reports every fatal Kotlin exception twice.
public func configureBugsnagForKotlin(_ config: BugsnagConfiguration) {
    overrideOriginalUnhandledValue()
    config.addOnSendError { event in
        // Drop Bugsnag's own report of the abort that terminates the app.
        // Drop it only when a Kotlin fatal is already recorded (markFatalCrashRecorded sets the flag).
        !event.unhandled || !event.featureFlags.contains { $0.name == kotlinCrashedFeatureFlag }
    }
    config.clearFeatureFlag(name: kotlinCrashedFeatureFlag)
}

/// Reference `CrashKiOSBugsnagSink` implementation.
///
/// Start Bugsnag through this class. The class prepares the configuration for Kotlin
/// crash handling. Then configure CrashKiOS with the returned sink:
/// ```swift
/// let sink = BugsnagSink.start(BugsnagConfiguration.loadConfig())
/// CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink: sink))
/// ```
/// Add `export("co.touchlab.crashkios:bugsnag")` AND `export("co.touchlab.crashkios:core")`
/// to the Kotlin framework for clean names.
public final class BugsnagSink: NSObject, CrashKiOSBugsnagSink {

    /// Prepares `config` with `configureBugsnagForKotlin(_:)`, starts Bugsnag,
    /// and returns the sink.
    public static func start(_ config: BugsnagConfiguration) -> BugsnagSink {
        configureBugsnagForKotlin(config)
        Bugsnag.start(with: config)
        return BugsnagSink()
    }

    public func leaveBreadcrumb(_ message: String) {
        Bugsnag.leaveBreadcrumb(withMessage: message)
    }

    public func notify(with exceptions: [NSException], handled: Bool) {
        guard let exception = exceptions.first else { return }
        // `Bugsnag.notify` persists unhandled events, so the caller can safely terminate afterwards.
        // https://github.com/bugsnag/bugsnag-cocoa/blob/6bcd46f5f8dc06ac26537875d501f02b27d219a9/Bugsnag/Client/BugsnagClient.m#L744
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

    public func addMetadata(_ value: Any, key: String, section: String) {
        Bugsnag.addMetadata(value, key: key, section: section)
    }

    public func markFatalCrashRecorded() {
        // Only the Kotlin unhandled-exception hook calls this method, right before termination.
        // A direct sendFatalException() call must never trigger it. That call would poison
        // the session, and the OnSendError filter would discard later genuine crashes.
        Bugsnag.addFeatureFlag(name: kotlinCrashedFeatureFlag)
    }
}

/// In Bugsnag 6.26.2+, the `originalUnhandledValue` property prevents Bugsnag from storing our
/// synthetic unhandled exceptions to disk. This function aliases it to `unhandled`.
/// https://github.com/bugsnag/bugsnag-cocoa/pull/1549
private func overrideOriginalUnhandledValue() {
    guard let handledStateClass = NSClassFromString("BugsnagHandledState"),
        let method = class_getInstanceMethod(handledStateClass, NSSelectorFromString("unhandled"))
    else {
        assertionFailure("CrashKiOS: BugsnagHandledState internals changed — Kotlin fatal crashes may not be persisted. Update CrashKiOSBugsnag.")
        return
    }
    // Bugsnag < 6.26.2 has no originalUnhandledValue. Nothing to override.
    guard let originalMethod = class_getInstanceMethod(handledStateClass, NSSelectorFromString("originalUnhandledValue"))
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
