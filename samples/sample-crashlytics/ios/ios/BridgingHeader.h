#ifndef BridgingHeader_h
#define BridgingHeader_h

// Copy of Sources/CrashKiOSCrashlyticsObjC/include/CrashKiOSCrashlyticsSink.h.
// The Kotlin framework's umbrella header only FORWARD-declares this protocol
// (`@protocol CrashKiOSCrashlyticsSink;`), so Swift needs the real declaration to
// conform to it. Apps consuming the CrashKiOSCrashlytics Swift package get it from
// the package (see the ios-spm sample) and don't need this file.
#import <Foundation/Foundation.h>

@protocol CrashKiOSCrashlyticsSink <NSObject>
- (void)logMessage:(NSString * _Nonnull)message;
- (void)recordHandledExceptionWithName:(NSString * _Nonnull)name
                                reason:(NSString * _Nonnull)reason
                        stackAddresses:(NSArray<NSNumber *> * _Nonnull)addresses;
- (void)recordFatalException:(NSException * _Nonnull)exception;
- (void)setCustomValue:(id _Nullable)value forKey:(NSString * _Nonnull)key;
- (void)setUserId:(NSString * _Nonnull)identifier;
@end

#endif /* BridgingHeader_h */
