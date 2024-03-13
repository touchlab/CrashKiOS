package co.touchlab.crashkios

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.File
import javax.inject.Inject

abstract class CrashlyticsFrameworkValueSource : ValueSource<File, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): File {
        val frameworkFile = findFrameworkBinaryFolder(
            execOperations = execOperations,
            zipUrl = "https://github.com/firebase/firebase-ios-sdk/releases/download/10.22.0/Firebase.zip",
            frameworkName = "FirebaseCrashlytics",
            zipRelativeFrameworkPath = "Firebase/FirebaseCrashlytics/FirebaseCrashlytics.xcframework"
        )

        return frameworkFile
    }
}