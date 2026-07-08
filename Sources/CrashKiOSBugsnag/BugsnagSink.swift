import Foundation
import ObjectiveC
import CrashKiOSBugsnagObjC
import Bugsnag

/// Feature flag used to mark the Kotlin termination crash.
/// Public so custom `CrashKiOSBugsnagSink` implementations can honor the same contract.
public let kotlinCrashedFeatureFlag = "crashkios.kotlin_crashed"

/// Prepares `config` for Kotlin crash handling: installs the OnSendError filter that
/// suppresses the duplicate termination crash following a recorded Kotlin fatal, and
/// works around Bugsnag 6.26.2+ refusing to persist synthetic unhandled events.
///
/// Custom sink implementations MUST call this before `Bugsnag.start(with: config)` —
/// otherwise every fatal Kotlin exception is reported twice.
public func configureBugsnagForKotlin(_ config: BugsnagConfiguration) {
    overrideOriginalUnhandledValue()
    config.addOnSendError { event in
        // Drop Bugsnag's own report of the abort that terminates the app after a
        // Kotlin fatal was already recorded (flagged via markFatalCrashRecorded).
        !event.unhandled || !event.featureFlags.contains { $0.name == kotlinCrashedFeatureFlag }
    }
    config.clearFeatureFlag(name: kotlinCrashedFeatureFlag)
}

/// Reference `CrashKiOSBugsnagSink` implementation.
///
/// Start Bugsnag through this class so the configuration is prepared for Kotlin
/// crash handling, then register the returned sink with the Kotlin side:
/// ```swift
/// let sink = BugsnagSink.start(BugsnagConfiguration.loadConfig())
/// BugsnagKt.registerBugsnagSink(sink: sink)
/// ```
/// (Add `export("co.touchlab.crashkios:bugsnag")` to the Kotlin framework for clean names.)
public final class BugsnagSink: NSObject, CrashKiOSBugsnagSink {

    /// Configures `config` via `configureBugsnagForKotlin(_:)`, starts Bugsnag,
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
        // Notify persists unhandled events, so the caller can safely terminate afterwards.
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
        // Called only by the Kotlin unhandled-exception hook, right before termination —
        // never on direct sendFatalException() calls, which would poison the session and
        // make the OnSendError filter discard later genuine crashes.
        Bugsnag.addFeatureFlag(name: kotlinCrashedFeatureFlag)
    }
}

/// In Bugsnag 6.26.2+ the `originalUnhandledValue` property prevents our synthetic
/// unhandled exceptions from being stored to disk; alias it to `unhandled`.
/// https://github.com/bugsnag/bugsnag-cocoa/pull/1549
private func overrideOriginalUnhandledValue() {
    guard let handledStateClass = NSClassFromString("BugsnagHandledState"),
          let originalMethod = class_getInstanceMethod(handledStateClass, NSSelectorFromString("originalUnhandledValue")),
          let method = class_getInstanceMethod(handledStateClass, NSSelectorFromString("unhandled"))
    else {
        // Loud, not silent: if Bugsnag renames these internals, fatal Kotlin crashes
        // would silently stop reaching the dashboard (>=6.26.2 refuses to persist them).
        assertionFailure("CrashKiOS: BugsnagHandledState internals changed — Kotlin fatal crashes may not be persisted. Update CrashKiOSBugsnag.")
        return
    }
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
