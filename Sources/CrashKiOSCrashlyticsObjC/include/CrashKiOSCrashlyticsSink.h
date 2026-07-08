#ifndef CrashKiOSCrashlyticsSink_h
#define CrashKiOSCrashlyticsSink_h

#import <Foundation/Foundation.h>

/// Receives crash-reporting events from Kotlin and forwards them to Firebase Crashlytics.
///
/// Implemented in Swift, inside the app's build graph — where FirebaseCrashlytics is
/// already linked — and registered with the Kotlin side via `registerCrashlyticsSink`.
///
/// Implementations MUST NOT throw: Kotlin cannot catch ObjC exceptions thrown across
/// this boundary (KT-53004), and the fatal path runs inside the unhandled-exception hook.
@protocol CrashKiOSCrashlyticsSink <NSObject>

/// Crashlytics `log:`.
- (void)logMessage:(NSString * _Nonnull)message;

/// A non-fatal exception. `addresses` are Kotlin stack-trace return addresses,
/// mappable to `FIRStackFrame stackFrameWithAddress:`.
- (void)recordHandledExceptionWithName:(NSString * _Nonnull)name
                                reason:(NSString * _Nonnull)reason
                        stackAddresses:(NSArray<NSNumber *> * _Nonnull)addresses;

/// A fatal exception. The process terminates right after this returns —
/// the record must be persisted synchronously (FIRCLSExceptionRecordNSException does).
/// `exception.callStackReturnAddresses` carries the Kotlin stack trace.
- (void)recordFatalException:(NSException * _Nonnull)exception;

/// Crashlytics `setCustomValue:forKey:`.
- (void)setCustomValue:(id _Nullable)value forKey:(NSString * _Nonnull)key;

/// Crashlytics `setUserID:`.
- (void)setUserId:(NSString * _Nonnull)identifier;

@end

#endif /* CrashKiOSCrashlyticsSink_h */
