#ifndef CrashKiOSBugsnagSink_h
#define CrashKiOSBugsnagSink_h

#import <Foundation/Foundation.h>

/// Receives crash-reporting events from Kotlin and forwards them to Bugsnag.
///
/// Implement this protocol in Swift, in the app's build graph, where Bugsnag is already
/// linked. Pass the implementation to the Kotlin side with
/// `CrashKiOS.shared.configure(crashReporting: BugsnagCrashReporting(sink:))`.
///
/// Contract for implementations (the shipped `BugsnagSink` follows all of it):
/// - MUST NOT throw. Kotlin cannot catch ObjC exceptions thrown across this
///   boundary (KT-53004), and the fatal path runs inside the unhandled-exception hook.
/// - MUST prepare the Bugsnag configuration with `configureBugsnagForKotlin(_:)`
///   (CrashKiOSBugsnag package) before `Bugsnag.start`. If it does not, Bugsnag reports
///   every fatal Kotlin exception twice: once from the Kotlin notify and once from the
///   termination abort.
@protocol CrashKiOSBugsnagSink <NSObject>

/// Bugsnag `leaveBreadcrumbWithMessage:`.
- (void)leaveBreadcrumb:(NSString * _Nonnull)message;

/// Notifies Bugsnag of a Kotlin exception. `exceptions` is `[main, cause, causeOfCause, ...]`.
/// The `callStackReturnAddresses` of each element carry its Kotlin stack trace.
/// When `handled` is NO, the implementation MUST persist the event synchronously before it
/// returns (`Bugsnag.notify` does). The process may terminate right after.
- (void)notifyWithExceptions:(NSArray<NSException *> * _Nonnull)exceptions handled:(BOOL)handled;

/// Bugsnag `addMetadata:key:section:`.
- (void)addMetadata:(id _Nonnull)value key:(NSString * _Nonnull)key section:(NSString * _Nonnull)section;

/// Only the Kotlin unhandled-exception hook calls this method. The hook calls it right after
/// the fatal notify, just before the process terminates. The implementation flags the
/// session (Bugsnag feature flag `crashkios.kotlin_crashed`). The OnSendError filter that
/// `configureBugsnagForKotlin(_:)` installs then drops Bugsnag's own report of the
/// termination abort. A direct `sendFatalException()` call does NOT trigger this method.
- (void)markFatalCrashRecorded;

@end

#endif /* CrashKiOSBugsnagSink_h */
