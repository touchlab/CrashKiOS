## The Problem

Crash reporter clients on iOS take the stack of active threads at the moment of crash. Kotlin on iOS, like Kotlin on the JVM, bubbles exceptions up until they are caught or reach the top of the call stack, at which point an unhandled exception hook is called. Because this differs from how iOS works, the crash report shows the point at which we call into Kotlin from Swift/Objc.

<img src="kotlinabort.png" alt="Abort report" style="zoom:50%;" />

We want to get the caught exception and record that instead:

<img src="kotlinlines.png" alt="Abort report" style="zoom: 67%;" />

That's what this library is for.