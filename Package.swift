// swift-tools-version: 5.5
// CrashKiOS Swift sinks — live in the app's build graph, where the crash-SDK
// binaries are already linked. See docs-plans/crashkios-linking-remediation-plan.md.

import PackageDescription

let package = Package(
    name: "CrashKiOS",
    platforms: [.iOS(.v15), .macOS(.v10_15), .tvOS(.v15), .watchOS(.v7)],
    products: [
        .library(
            name: "CrashKiOSCrashlytics",
            targets: ["CrashKiOSCrashlytics"]
        ),
        .library(
            name: "CrashKiOSBugsnag",
            targets: ["CrashKiOSBugsnag"]
        ),
    ],
    dependencies: [
        .package(
            url: "https://github.com/firebase/firebase-ios-sdk.git",
            "11.0.0"..<"13.0.0"
        ),
        .package(
            url: "https://github.com/bugsnag/bugsnag-cocoa.git",
            "6.22.1"..<"7.0.0"
        ),
    ],
    targets: [
        // Header-only targets: they own the sink protocols (cinterop'd by the Kotlin
        // modules) so Swift sinks compile without importing the app's Kotlin framework.
        .target(
            name: "CrashKiOSCrashlyticsObjC",
            path: "Sources/CrashKiOSCrashlyticsObjC"
        ),
        .target(
            name: "CrashKiOSCrashlytics",
            dependencies: [
                .target(name: "CrashKiOSCrashlyticsObjC"),
                .product(name: "FirebaseCrashlytics", package: "firebase-ios-sdk"),
            ],
            path: "Sources/CrashKiOSCrashlytics"
        ),
        .target(
            name: "CrashKiOSBugsnagObjC",
            path: "Sources/CrashKiOSBugsnagObjC"
        ),
        .target(
            name: "CrashKiOSBugsnag",
            dependencies: [
                .target(name: "CrashKiOSBugsnagObjC"),
                .product(name: "Bugsnag", package: "bugsnag-cocoa"),
            ],
            path: "Sources/CrashKiOSBugsnag"
        ),
    ]
)
