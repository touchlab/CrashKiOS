@file:OptIn(ExperimentalForeignApi::class)

package co.touchlab.crashkios.bugsnag

import com.rickclephas.kmp.nsexceptionkt.core.asNSException
import com.rickclephas.kmp.nsexceptionkt.core.causes
import com.rickclephas.kmp.nsexceptionkt.core.wrapUnhandledExceptionHook
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSException

public fun startBugsnag(config: BugsnagConfiguration) {
    configureBugsnag(config)
    Bugsnag.startWithConfiguration(config)
    setBugsnagUnhandledExceptionHook()
    enableBugsnag()
}

/**
 * Configures Bugsnag to ignore the Kotlin termination crash.
 */
public fun configureBugsnag(config: BugsnagConfiguration) {
    NSExceptionKt_OverrideBugsnagHandledStateOriginalUnhandledValue()
    NSExceptionKt_BugsnagConfigAddOnSendErrorBlock(config) { event ->
        if (event == null) return@NSExceptionKt_BugsnagConfigAddOnSendErrorBlock true
        !event.unhandled || event.featureFlags.none { (it as BugsnagFeatureFlag).name == kotlinCrashedFeatureFlag }
    }
    config.clearFeatureFlagWithName(kotlinCrashedFeatureFlag)
}

/**
 * Sets the unhandled exception hook such that all unhandled exceptions are logged to Bugsnag as fatal exceptions.
 * If an unhandled exception hook was already set, that hook will be invoked after the exception is logged.
 * Note: once the exception is logged the program will be terminated.
 * @see wrapUnhandledExceptionHook
 */
public fun setBugsnagUnhandledExceptionHook(): Unit = wrapUnhandledExceptionHook { throwable ->
    val exception = throwable.asNSException()
    val causes = throwable.causes.map { it.asNSException() }
    // Notify will persist unhandled events, so we can safely terminate afterwards.
    // https://github.com/bugsnag/bugsnag-cocoa/blob/6bcd46f5f8dc06ac26537875d501f02b27d219a9/Bugsnag/Client/BugsnagClient.m#L744
    Bugsnag.notify(exception) { event ->
        if (event == null) return@notify true
        event.unhandled = true
        event.severity = BSGSeverity.BSGSeverityError
        if (causes.isNotEmpty()) {
            event.errors += causes.map { it.asBugsnagError() }
        }
        true
    }
    Bugsnag.addFeatureFlagWithName(kotlinCrashedFeatureFlag)
}

/**
 * Feature flag used to mark the Kotlin termination crash.
 */
@Suppress("ktlint:standard:property-naming")
private const val kotlinCrashedFeatureFlag = "crashkios.kotlin_crashed"

/**
 * Converts `this` [NSException] to a [BugsnagError].
 */
internal fun NSException.asBugsnagError(): BugsnagError = BugsnagError().apply {
    errorClass = name
    errorMessage = reason
    stacktrace = BugsnagStackframe.stackframesWithCallStackReturnAddresses(callStackReturnAddresses)
    type = BSGErrorType.BSGErrorTypeCocoa
}
