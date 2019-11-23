//
//  ContentView.swift
//  iosApp
//
//  Created by Kevin Galligan on 10/7/19.
//  Copyright © 2019 Kevin Galligan. All rights reserved.
//

import SwiftUI
import sample

struct ContentView: View {
    let cb = CrashBot()
    var body: some View {
        
        VStack(alignment: .leading, spacing: 50) {
            Button(action: {
                self.cb.goCrash()
            }) {
                Text("Crash Main Thread in Kotlin")
            }
            
            Button(action: {
                self.cb.differentPath()
            }) {
                Text("Crash Main Thread differentPath Kotlin")
            }
            
            Button(action: {
                self.cb.goCrashBackground()
            }) {
                Text("Crash Background Thread in Kotlin")
            }
            
            Button(action: {
                self.cb.manualCatch()
            }) {
                Text("Manual catch and report")
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
