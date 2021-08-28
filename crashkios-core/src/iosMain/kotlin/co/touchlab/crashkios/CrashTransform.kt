package co.touchlab.crashkios

fun transformException(t: Throwable, block: (String, String, addresses: List<Long>) -> Unit) {
    fun throwableBoilerplate(frameString: String, lookFor: String) =
        !frameString.contains("kotlin.${lookFor}")
                &&
                !frameString.contains("${lookFor}.<init>")

    val addresses: List<Long> = t.getStackTraceAddresses()

    var index = 0

    val stackTrace = t.getStackTrace()
    for (element in stackTrace) {
        if (
            throwableBoilerplate(element, "Exception")
            ||
            throwableBoilerplate(element, "Throwable")
        ) {
            break
        }

        index++
    }

    val trimmedAddresses = addresses.subList(index, addresses.size)

    block(
        t::class.simpleName ?: "(Unknown Type)",
        t.message ?: "",
        trimmedAddresses
    )
}