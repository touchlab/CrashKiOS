/*
 * Copyright (c) 2021 Touchlab
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package co.touchlab.crashkiossample

import co.touchlab.crashkios.crashlytics.CrashlyticsKotlin

class SampleCommon {
    private var count = 0
    fun onClick() {
        count++
        CrashlyticsKotlin.logMessage(message = "Common click count: $count")
    }

    fun logException(){
        CrashlyticsKotlin.sendHandledException(throwable = IllegalStateException("Handled, Common click count: $count"))
    }
}
