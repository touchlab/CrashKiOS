// The following are snippets from the Firebase Crashlytics iOS SDK used to generate Kotlin stubs.
//
// https://github.com/firebase/firebase-ios-sdk/blob/85781d0c7661af0bd5d11c5b00cf47e6a1c0adff/Crashlytics/Crashlytics/Public/FirebaseCrashlytics/FIRCrashlytics.h
//
// Copyright 2019-2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#import <Foundation/Foundation.h>
#import <FIRExceptionModel.h>

@interface FIRCrashlytics : NSObject

+ (nonnull instancetype)crashlytics;
- (void)recordExceptionModel:(nonnull FIRExceptionModel *)exceptionModel;
- (void)log:(NSString *)msg;
- (void)setCustomValue:(nullable id)value forKey:(NSString *)key;

@end
