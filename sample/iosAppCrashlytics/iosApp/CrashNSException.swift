//
//  CrashNSException.swift
//  iosApp
//
//  Created by Kevin Galligan on 10/10/19.
//  Copyright © 2019 Kevin Galligan. All rights reserved.
//

import Foundation
import sample
import Crashlytics

class CrashlyticsCrashHandler: CrashkiosCrashHandler {
    override func crashParts(
        addresses: [KotlinLong],
        exceptionType: String,
        message: String) {
        let clsStackTrace = addresses.map {
            CLSStackFrame(address: UInt(truncating: $0))
        }

        Crashlytics.sharedInstance().recordCustomExceptionName(
            exceptionType,
            reason: message,
            frameArray: clsStackTrace
        )
    }
}
