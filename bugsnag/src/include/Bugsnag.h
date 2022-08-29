// The following are snippets from the Bugsnag Cocoa SDK used to generate Kotlin stubs.
//
// https://github.com/bugsnag/bugsnag-cocoa/blob/6bcd46f5f8dc06ac26537875d501f02b27d219a9/Bugsnag/include/Bugsnag/Bugsnag.h
//
// Copyright (c) 2012 Bugsnag, https://bugsnag.com/
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the Software
// is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

#import <Foundation/Foundation.h>
#import <BugsnagConfiguration.h>

NS_ASSUME_NONNULL_BEGIN

// -----------------------------------------------------------------------------
// MARK: - <BugsnagClassLevelMetadataStore>
// -----------------------------------------------------------------------------

/**
 * A class-level protocol supporting the MetadataStore interface
 */
@protocol BugsnagClassLevelMetadataStore <NSObject>

@required

/**
 * Merge supplied and existing metadata.
 *
 * - Non-null values will replace existing values for identical keys.
 *
 * - Null values will remove the existing key/value pair if the key exists.
 *   Where null-valued keys do not exist they will not be set.  (Since ObjC
 *   dicts can't store 'nil' directly we assume [NSNUll null])
 *
 * - Tabs are only created if at least one value is valid.
 *
 * - Invalid values (i.e. unserializable to JSON) are logged and ignored.
 *
 * @param metadata A dictionary of string -> id key/value pairs.
 *                 Values should be serializable to JSON.
 *
 * @param sectionName The name of the metadata section
 *
 */
+ (void)addMetadata:(NSDictionary *_Nonnull)metadata
          toSection:(NSString *_Nonnull)sectionName
    NS_SWIFT_NAME(addMetadata(_:section:));

/**
 * Add a piece of metadata to a particular key in a particular section.
 *
 * - Non-null values will replace existing values for identical keys.
 *
 * - Null values will remove the existing key/value pair if the key exists.
 *   Where null-valued keys do not exist they will not be set.  (Since ObjC
 *   dicts can't store 'nil' directly we assume [NSNUll null])
 *
 * - Tabs are only created if at least one value is valid.
 *
 * - Invalid values (i.e. unserializable to JSON) are logged and ignored.
 *
 * @param metadata A dictionary of string -> id key/value pairs.
 *                 Values should be serializable to JSON.
 *
 * @param key The metadata key to store the value under
 *
 * @param sectionName The name of the metadata section
 *
 */
+ (void)addMetadata:(id _Nullable)metadata
            withKey:(NSString *_Nonnull)key
          toSection:(NSString *_Nonnull)sectionName
    NS_SWIFT_NAME(addMetadata(_:key:section:));

@end

@interface Bugsnag : NSObject <BugsnagClassLevelMetadataStore>

+ (void)notify:(NSException *_Nonnull)exception block:(BugsnagOnErrorBlock _Nullable)block;
+ (void)addFeatureFlagWithName:(nonnull NSString *)name;
+ (void)leaveBreadcrumbWithMessage:(NSString *_Nonnull)message;

@end



NS_ASSUME_NONNULL_END
