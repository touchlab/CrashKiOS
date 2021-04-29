//
//  CrashNSException.swift
//  iosApp
//
//  Created by Kevin Galligan on 10/10/19.
//  Copyright © 2019 Kevin Galligan. All rights reserved.
//

import Foundation
import sample
import FirebaseCrashlytics

class CrashlyticsCrashHandler: CrashkiosCrashHandler {
    override func crashParts(
        addresses: [KotlinLong],
        exceptionType: String,
        message: String) {
        let exceptionModel = ExceptionModel(name: exceptionType, reason: message)
        exceptionModel.stackTrace = addresses.map {
            StackFrame(address: UInt(truncating: $0))
        }
        Crashlytics.crashlytics().record(exceptionModel: exceptionModel)
    }
}
