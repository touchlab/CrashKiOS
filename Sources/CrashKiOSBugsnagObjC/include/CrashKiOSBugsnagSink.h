#ifndef CrashKiOSBugsnagSink_h
#define CrashKiOSBugsnagSink_h

#import <Foundation/Foundation.h>

/// Receives crash-reporting events from Kotlin and forwards them to Bugsnag.
///
/// Implemented in Swift, inside the app's build graph — where Bugsnag is already
/// linked — and registered with the Kotlin side via `registerBugsnagSink`.
///
/// Contract for implementations (the shipped `BugsnagSink` does all of this):
/// - MUST NOT throw: Kotlin cannot catch ObjC exceptions thrown across this
///   boundary (KT-53004), and the fatal path runs inside the unhandled-exception hook.
/// - MUST prepare the Bugsnag configuration with `configureBugsnagForKotlin(_:)`
///   (CrashKiOSBugsnag package) before `Bugsnag.start`, otherwise every fatal Kotlin
///   exception is reported twice (the Kotlin notify plus Bugsnag's own report of the
///   termination abort).
@protocol CrashKiOSBugsnagSink <NSObject>

/// Bugsnag `leaveBreadcrumbWithMessage:`.
- (void)leaveBreadcrumb:(NSString * _Nonnull)message;

/// Notify Bugsnag of a Kotlin exception. `exceptions` is `[main, cause, causeOfCause, ...]`;
/// each element's `callStackReturnAddresses` carries its Kotlin stack trace.
/// When `handled` is NO the event must be persisted synchronously before returning
/// (`Bugsnag.notify` does) — the process may terminate right after.
- (void)notifyWithExceptions:(NSArray<NSException *> * _Nonnull)exceptions handled:(BOOL)handled;

/// Bugsnag `addMetadata:key:section:`.
- (void)addMetadata:(id _Nonnull)value key:(NSString * _Nonnull)key section:(NSString * _Nonnull)section;

/// Called by the Kotlin unhandled-exception hook — and ONLY that hook — right after
/// the fatal notify, just before the process terminates. Implementations flag the
/// session (Bugsnag feature flag `crashkios.kotlin_crashed`) so the OnSendError filter
/// installed by `configureBugsnagForKotlin(_:)` drops Bugsnag's own report of the
/// termination abort. Direct `sendFatalException()` calls do NOT trigger this.
- (void)markFatalCrashRecorded;

@end

#endif /* CrashKiOSBugsnagSink_h */
