#ifndef BridgingHeader_h
#define BridgingHeader_h

// Copy of Sources/CrashKiOSBugsnagObjC/include/CrashKiOSBugsnagSink.h.
// The Kotlin framework's umbrella header only FORWARD-declares this protocol
// (`@protocol CrashKiOSBugsnagSink;`), so Swift needs the real declaration to
// conform to it. Apps consuming the CrashKiOSBugsnag Swift package get it from
// the package and don't need this file.
#import <Foundation/Foundation.h>

@protocol CrashKiOSBugsnagSink <NSObject>
- (void)leaveBreadcrumb:(NSString * _Nonnull)message;
- (void)notifyWithExceptions:(NSArray<NSException *> * _Nonnull)exceptions handled:(BOOL)handled;
- (void)addMetadata:(id _Nonnull)value key:(NSString * _Nonnull)key section:(NSString * _Nonnull)section;
- (void)markFatalCrashRecorded;
@end

#endif /* BridgingHeader_h */
