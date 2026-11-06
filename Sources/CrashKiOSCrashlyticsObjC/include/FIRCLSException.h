#ifndef CrashKiOSFIRCLSException_h
#define CrashKiOSFIRCLSException_h

// Declaration copied from firebase-ios-sdk's private header:
// https://github.com/firebase/firebase-ios-sdk/blob/main/Crashlytics/Crashlytics/Handlers/FIRCLSException.h
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

// Records `exception` as the session's fatal exception and persists it synchronously.
// Private Crashlytics SPI, resolved HARD at app link time via the FirebaseCrashlytics
// product this target depends on — nothing dynamic is left for the App Store's
// deployment-processing strip pass to break (the CrashKiOS <= 0.9.0 failure mode).
// Fallback if Firebase ever removes it: -[FIRCrashlytics recordOnDemandExceptionModel:].
extern void FIRCLSExceptionRecordNSException(NSException *exception);

#endif /* CrashKiOSFIRCLSException_h */
