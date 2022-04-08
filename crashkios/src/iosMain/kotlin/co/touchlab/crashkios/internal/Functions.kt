package co.touchlab.crashkios.internal

import kotlin.native.concurrent.freeze

internal inline fun <T> T.maybeFreeze(): T = if (Platform.memoryModel == MemoryModel.STRICT) {
    this.freeze()
} else {
    this
}