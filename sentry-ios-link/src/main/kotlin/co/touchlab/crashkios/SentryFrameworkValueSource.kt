package co.touchlab.crashkios

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class SentryFrameworkValueSource : ValueSource<File, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): File {
        val frameworkFile = findFrameworkBinaryFolder(
            execOperations,
            "https://github.com/getsentry/sentry-cocoa/releases/download/8.21.0/Sentry.xcframework.zip",
            "Sentry"
        )

        return frameworkFile
    }
}