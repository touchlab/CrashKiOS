package co.touchlab.crashkios.core

/**
 * Marks CrashKiOS API that only the CrashKiOS SDK modules may use. There are no guarantees about the binary nor source compatibility. These
 * APIs are not meant to ber used in client code.
 */
@RequiresOptIn(
    message = "This is internal CrashKiOS API and is subject to change without notice.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
)
public annotation class InternalCrashKiOSApi
